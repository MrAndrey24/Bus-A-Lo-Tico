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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.googleServices)
}

kotlin {
  compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }

  dependencies {
    implementation(projects.sharedLogic)

    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.activity.compose)

    // MapBox
    implementation(libs.mapbox.android)
    implementation(libs.mapbox.compose.android)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
  }
}


android {
  namespace = "com.example.busalotico"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.example.busalotico"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
    manifestPlaceholders["MAPBOX_ACCESS_TOKEN"] =
        (project.findProperty("MAPBOX_ACCESS_TOKEN") as String?) ?: ""
  }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}
