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
    implementation(projects.composeApp)
    implementation(projects.shared)

    implementation(libs.compose.uiToolingPreview)
    implementation(libs.androidx.activity.compose)

    // MapBox
    implementation(libs.mapbox.android)
    implementation(libs.mapbox.compose.android)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
  }
}

// To get more info: https://developer.android.com/build/build-variants
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }
}