/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

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
