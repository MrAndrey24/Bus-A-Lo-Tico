plugins {
  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
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
  ratchetFrom("origin/main")

  kotlin {
    target("**/src/**/*.kt")
    targetExclude("**/build/**", "**/generated/**")
    ktfmt()
  }

  kotlinGradle {
    target("*.gradle.kts", "**/*.gradle.kts")
    targetExclude("**/build/**")
    ktfmt()
  }
}
