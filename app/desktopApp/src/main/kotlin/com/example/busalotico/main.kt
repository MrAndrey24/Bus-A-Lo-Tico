/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.busalotico

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.example.busalotico.ui.components.desktop.DesktopCenteredLoading
import com.example.busalotico.ui.screens.company.CompanyAdminDesktopApp
import com.example.busalotico.ui.screens.desktop.DesktopFirebaseErrorApp
import com.example.busalotico.ui.screens.desktop.DesktopLoginScreen
import com.example.busalotico.ui.screens.desktop.DesktopUnauthorizedScreen
import com.example.busalotico.ui.theme.DesktopAdminTheme
import com.example.sharedlogic.di.initKoin
import com.example.sharedlogic.domain.entities.UserRole
import com.example.sharedlogic.presentation.auth.AuthViewModel
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.initialize
import java.io.File
import java.util.Properties
import org.koin.compose.viewmodel.koinViewModel

fun main() {
  val firebaseInitError = runCatching { initializeDesktopFirebase() }.exceptionOrNull()
  if (firebaseInitError == null) {
    initKoin()
  }
  application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Bus A Lo Tico · Gestión de empresa",
        state = WindowState(width = 1360.dp, height = 920.dp),
    ) {
      DesktopAdminTheme {
        if (firebaseInitError != null) {
          DesktopFirebaseErrorApp(firebaseInitError)
        } else {
          DesktopRootApp()
        }
      }
    }
  }
}

@Composable
private fun DesktopRootApp() {
  val authViewModel = koinViewModel<AuthViewModel>()
  val authUiState by authViewModel.uiState.collectAsState()

  when {
    authUiState.isLoading -> DesktopCenteredLoading()
    !authUiState.isAuthenticated ->
        DesktopLoginScreen(
            isLoading = false,
            errorMessage = authUiState.errorMessage,
            onSignIn = authViewModel::signIn,
        )
    authUiState.currentUser?.role != UserRole.ADMIN ->
        DesktopUnauthorizedScreen(
            userName = authUiState.currentUser?.name.orEmpty(),
            role = authUiState.currentUser?.role,
            onSignOut = authViewModel::signOut,
        )
    else ->
        CompanyAdminDesktopApp(
            currentUserName = authUiState.currentUser?.name.orEmpty(),
            currentUserCompanyId = authUiState.currentUser?.companyId,
            onSignOut = authViewModel::signOut,
        )
  }
}

private fun initializeDesktopFirebase() {
  val appContext = Application()
  initializeFirebasePlatform()
  if (Firebase.apps(appContext).isNotEmpty()) return

  val properties = Properties()
  val stream =
      checkNotNull(
          Thread.currentThread().contextClassLoader.getResourceAsStream("firebase.properties")
      ) {
        "No se encontró desktopApp/src/jvmMain/resources/firebase.properties"
      }
  stream.use(properties::load)

  val projectId = properties.getProperty("projectId")?.trim().orEmpty()
  val applicationId = properties.getProperty("applicationId")?.trim().orEmpty()
  val apiKey = properties.getProperty("apiKey")?.trim().orEmpty()

  check(projectId.isNotBlank()) { "Falta projectId en firebase.properties" }
  check(applicationId.isNotBlank()) { "Falta applicationId en firebase.properties" }
  check(apiKey.isNotBlank()) { "Falta apiKey en firebase.properties" }

  Firebase.initialize(
      context = appContext,
      options =
          FirebaseOptions(
              applicationId = applicationId,
              apiKey = apiKey,
              storageBucket =
                  properties.getProperty("storageBucket")?.trim().takeUnless { it.isNullOrBlank() },
              projectId = projectId,
              gcmSenderId =
                  properties.getProperty("gcmSenderId")?.trim().takeUnless { it.isNullOrBlank() },
              authDomain =
                  properties.getProperty("authDomain")?.trim().takeUnless { it.isNullOrBlank() },
          ),
  )
}

private fun initializeFirebasePlatform() {
  FirebasePlatform.initializeFirebasePlatform(DesktopFirebasePlatform)
}

private object DesktopFirebasePlatform : FirebasePlatform() {
  private val values = mutableMapOf<String, String>()
  private val baseDir =
      File(System.getProperty("user.home"), ".busalotico-desktop").apply { mkdirs() }

  override fun store(key: String, value: String) {
    values[key] = value
  }

  override fun retrieve(key: String): String? = values[key]

  override fun clear(key: String) {
    values.remove(key)
  }

  override fun log(msg: String) {
    println("[FirebaseDesktop] $msg")
  }

  override fun getDatabasePath(name: String): File = File(baseDir, name)
}
