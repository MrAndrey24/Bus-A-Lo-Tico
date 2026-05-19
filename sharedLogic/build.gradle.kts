/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.androidKotlinMultiplatformLibrary)
  alias(libs.plugins.androidLint)
}

kotlin {
  iosArm64()
  iosSimulatorArm64()

  android {
    namespace = "com.example.busalotico.sharedLogic"
    compileSdk {
      version = release(36) {
        minorApiLevel = 1
      }
    }
    minSdk = 24

    withHostTestBuilder {
    }

    withDeviceTestBuilder {
      sourceSetTreeName = "test"
    }.configure {
      instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }

  jvm("desktop")

  val xcfName = "sharedLogicKit"

  iosX64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  iosArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  iosSimulatorArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }

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
        implementation(libs.ktor.client.okhttp)

        // GitLive Firebase
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
        // Darwin
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
