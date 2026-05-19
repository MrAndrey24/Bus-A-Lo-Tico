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
import java.util.Properties

rootProject.name = "BusALoTico"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val localProperties = Properties()
val localPropertiesFile = File(rootDir, "local.properties")

if (localPropertiesFile.exists()) {
  localPropertiesFile.inputStream().use { it -> localProperties.load(it) }
}

val MAPBOX_DOWNLOADS_TOKEN = localProperties.getProperty("MAPBOX_DOWNLOADS_TOKEN") ?: ""

pluginManagement {
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
        includeGroupAndSubgroups("org.chromium.net")
      }
    }
    mavenCentral()
    maven {
      url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
      credentials {
        username = "mapbox"
        password = MAPBOX_DOWNLOADS_TOKEN
      }
      authentication { create<BasicAuthentication>("basic") }
    }
  }
}

include(":app:androidApp")
include(":app:desktopApp")
include(":app:shared")
include(":sharedLogic")
