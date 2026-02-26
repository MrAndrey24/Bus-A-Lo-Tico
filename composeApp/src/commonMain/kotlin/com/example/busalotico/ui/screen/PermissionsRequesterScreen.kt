package com.example.busalotico.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.busalotico.data.PermissionRequest
import com.example.busalotico.ui.components.permission.PermissionsDeniedScreen
import com.example.busalotico.ui.components.permission.PermissionsRequestScreen
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch

@Composable
fun PermissionsRequesterScreen(
    permissions: List<PermissionRequest>,
    onAllPermissionsGranted: () -> Unit = {},
    content: @Composable (PermissionsController) -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    BindEffect(controller)

    // 1. Initialize states to NotDetermined
    var permissionStates by remember {
        mutableStateOf(permissions.associate { it.permission to PermissionState.NotDetermined })
    }

    // 2. Centralized helper: Now launches a coroutine to handle the suspend function
    val refreshPermissions: () -> Unit = {
        scope.launch {
            permissionStates = permissions.associate { req ->
                req.permission to controller.getPermissionState(req.permission)
            }
        }
    }

    // 3. Initial check when the screen is first composed
    LaunchedEffect(Unit) {
        refreshPermissions()
    }

    // 4. React to App Lifecycle (e.g., returning from Android App Settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Derived states
    val allGranted = permissionStates.values.all { it == PermissionState.Granted }
    val anyPermanentlyDenied = permissionStates.values.any { it == PermissionState.DeniedAlways }

    // Safely trigger callback when state changes to allGranted
    LaunchedEffect(allGranted) {
        if (allGranted) {
            onAllPermissionsGranted()
        }
    }

    when {
        allGranted -> {
            content(controller)
        }

        anyPermanentlyDenied -> {
            PermissionsDeniedScreen(
                permissions = permissions,
                permissionStates = permissionStates,
                onOpenSettings = { controller.openAppSettings() }
            )
        }

        else -> {
            PermissionsRequestScreen(
                permissions = permissions,
                permissionStates = permissionStates,
                onRequestPermissions = {
                    scope.launch {
                        for (req in permissions) {
                            if (controller.getPermissionState(req.permission) != PermissionState.Granted) {
                                try {
                                    controller.providePermission(req.permission)
                                } catch (e: DeniedAlwaysException) {
                                    // Caught permanently denied; next refresh will handle it
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        // Refresh once all prompts are completed
                        refreshPermissions()
                    }
                }
            )
        }
    }
}