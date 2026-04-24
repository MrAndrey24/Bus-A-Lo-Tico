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
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
}

kotlin {
  jvm()

  sourceSets {
    jvmMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.ui)
      implementation(libs.material.icons.extended)
      implementation(libs.firebase.app)
      implementation(projects.shared)

      implementation(project.dependencies.platform(libs.koin.bom))
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)
    }
  }
}

compose.desktop {
  application { mainClass = "com.example.busalotico.desktop.CompanyAdminDesktopKt" }
}
