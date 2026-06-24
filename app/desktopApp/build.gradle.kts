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
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán,
 * Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
plugins {
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
}

dependencies {
  implementation(compose.desktop.currentOs)
  implementation(libs.compose.runtime)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.ui)
  implementation(libs.material.icons.extended)
  implementation(libs.firebase.app)
  implementation(projects.sharedLogic)

  implementation(project.dependencies.platform(libs.koin.bom))
  implementation(libs.koin.compose)
  implementation(libs.koin.compose.viewmodel)
}

compose.desktop {
  application {
    mainClass = "com.example.busalotico.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "com.example.busalotico"
      packageVersion = "1.0.0"
    }
  }
}
