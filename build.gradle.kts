/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.composeMultiplatform) apply false
  alias(libs.plugins.composeCompiler) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.kotlinSerialization) apply false
  alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
  alias(libs.plugins.androidLint) apply false
  alias(libs.plugins.googleServices) apply false
  alias(libs.plugins.spotless)
}

spotless {
  kotlin {
    target("**/src/**/*.kt")
    targetExclude("**/build/**", "**/generated/**")
    ktfmt()
    licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"), "(^(?![\\/ ]\\*).*$)")
  }

  kotlinGradle {
    target("*.gradle.kts", "**/*.gradle.kts")
    targetExclude("**/build/**")
    ktfmt()
    licenseHeaderFile(rootProject.file("spotless/spotless.license.kt"), "(^(?![\\/ ]\\*).*$)")
  }
}
