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
  application {
    mainClass = "com.example.busalotico.desktop.CompanyAdminDesktopKt"
  }
}
