package com.example.busalotico.config

import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole

object DevSessionConfig {

    const val BYPASS_AUTH = false

    val devUser = UserProfile(
        id = "dev-user",
        email = "dev@busalotico.local",
        name = "Usuario Dev",
        role = UserRole.ADMIN
    )
}
