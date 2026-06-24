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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKotlinMultiplatformLibrary)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
}

kotlin {
  listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = true
    }
  }

  android {
    namespace = "com.example.busalotico.app.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    minSdk = libs.versions.android.minSdk.get().toInt()

    compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    androidResources { enable = true }
    withHostTest { isIncludeAndroidResources = true }
  }

  sourceSets {
    androidMain.dependencies {
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.activity.compose)

      // MapBox
      implementation(libs.mapbox.android)
      implementation(libs.mapbox.compose.android)
    }
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodelCompose)
      implementation(libs.androidx.lifecycle.runtimeCompose)
      implementation(libs.material.icons.core)
      implementation(libs.material.icons.extended)

      // Shared Logic Module
      implementation(projects.sharedLogic)

      // Koin
      implementation(project.dependencies.platform(libs.koin.bom))
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)
      implementation(libs.koin.compose.viewmodel.navigation)

      // Moko
      implementation(libs.moko.permissions)
      implementation(libs.moko.permissions.compose)
      implementation(libs.moko.permissions.geo)

      // Nav
      implementation(libs.navigation.compose)
      implementation(libs.kotlinx.datetime)
    }
    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

dependencies { androidRuntimeClasspath(libs.compose.uiTooling) }
