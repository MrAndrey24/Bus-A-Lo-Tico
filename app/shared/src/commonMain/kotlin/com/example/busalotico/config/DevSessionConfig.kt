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
package com.example.busalotico.config

import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole

object DevSessionConfig {

  const val BYPASS_AUTH = false

  val devUser =
      UserProfile(
          id = "dev-user",
          email = "dev@busalotico.local",
          name = "Usuario Dev",
          role = UserRole.ADMIN,
      )
}
