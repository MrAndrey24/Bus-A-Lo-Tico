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
package com.example.sharedlogic.domain.entities

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val companyId: String? = null,
    val favoriteRouteIds: Set<String> = emptySet(),
)

enum class UserRole {
  PASSENGER,
  DRIVER,
  ADMIN,
}
