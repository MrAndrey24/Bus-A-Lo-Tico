package com.example.busalotico.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.busalotico.data.PermissionRequest
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch

@Composable
fun PermissionsRequesterScreen(
    permissions: List<PermissionRequest>,
    snackbarHostState: SnackbarHostState,
    onAllPermissionsGranted: () -> Unit = {},
    content: @Composable (PermissionsController, Boolean) -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    BindEffect(controller)

    var permissionStates by remember {
        mutableStateOf(permissions.associate { it.permission to PermissionState.NotDetermined })
    }

    var isSkipped by remember { mutableStateOf(false) }

    var isRequesting by remember { mutableStateOf(false) }

    val refreshPermissions: () -> Unit = {
        scope.launch {
            permissionStates = permissions.associate { req ->
                req.permission to controller.getPermissionState(req.permission)
            }
        }
    }

    val onRequestPermissionsClick: () -> Unit = {
        scope.launch {
            isRequesting = true

            for (req in permissions) {
                if (controller.getPermissionState(req.permission) != PermissionState.Granted) {
                    try {
                        controller.providePermission(req.permission)
                    } catch (e: DeniedAlwaysException) {
                    } catch (e: DeniedException) {
                        val result = snackbarHostState.showSnackbar(
                            message = "${req.title} is required for this function.",
                            actionLabel = "Try again",
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            try { controller.providePermission(req.permission) } catch (e: Exception) { }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            permissionStates = permissions.associate { req ->
                req.permission to controller.getPermissionState(req.permission)
            }

            isRequesting = false
        }
    }

    LaunchedEffect(Unit) { refreshPermissions() }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && !isRequesting) {
                refreshPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val allGranted = permissionStates.values.all { it == PermissionState.Granted }
    val anyPermanentlyDenied = permissionStates.values.any { it == PermissionState.DeniedAlways }

    LaunchedEffect(allGranted) {
        if (allGranted) onAllPermissionsGranted()
    }

    when {
        allGranted -> {
            content(controller, true)
        }

        isSkipped -> {
            content(controller, false)
        }

        isRequesting -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> {
            EnableLocationScreen(
                onAllowClick = { onRequestPermissionsClick() },
                onDenyClick = { isSkipped = true }
            )
        }
    }
}