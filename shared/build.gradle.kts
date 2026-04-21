/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.androidKotlinMultiplatformLibrary)
  alias(libs.plugins.androidLint)
}

kotlin {
  jvm("desktop")

  // Target declarations
  androidLibrary {
    namespace = "com.example.shared"
    compileSdk = 36
    minSdk = 24

    withHostTestBuilder {}

    withDeviceTestBuilder { sourceSetTreeName = "test" }
        .configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
  }

  val xcfName = "sharedKit"

  iosX64 { binaries.framework { baseName = xcfName } }

  iosArm64 { binaries.framework { baseName = xcfName } }

  iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlin.stdlib)

        // Koin
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.core)
        implementation(libs.koin.compose.viewmodel)

        // Coroutine
        implementation(libs.kotlinx.coroutines.core)

        // DateTime
        implementation(libs.kotlinx.datetime)

        // Ktor Core
        implementation(project.dependencies.platform(libs.ktor.bom))
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        // Firebase Kotlin SDK (GitLive)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.firestore)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.koin.test)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.ktor.client.mock)
      }
    }

    androidMain {
      dependencies {
        // --- AQUÍ ESTÁ EL ARREGLO PARA EL ERROR ---
        // Motor de Ktor para Android (OkHttp es el recomendado)
        implementation(libs.ktor.client.okhttp)

        // Requerido por GitLive Firebase Android artifacts
        implementation(project.dependencies.platform(libs.firebase.bom))

        // Koin & Coroutines Android
        implementation(libs.koin.android)
        implementation(libs.kotlinx.coroutines.android)
      }
    }

    getByName("androidDeviceTest") {
      dependencies {
        implementation(libs.androidx.runner)
        implementation(libs.androidx.core)
        implementation(libs.androidx.testExt.junit)
      }
    }

    iosMain {
      dependencies {
        // Motor de Ktor para iOS (Darwin)
        implementation(libs.ktor.client.darwin)
      }
    }

    val desktopMain by getting {
      dependencies {
        implementation(libs.ktor.client.cio)
        implementation(libs.kotlinx.coroutines.swing)
      }
    }
  }
}
