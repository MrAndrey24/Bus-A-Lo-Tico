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
