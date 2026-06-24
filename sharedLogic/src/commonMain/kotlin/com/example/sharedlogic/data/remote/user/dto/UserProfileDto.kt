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
package com.example.sharedlogic.data.remote.user.dto

import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val email: String = "",
    val name: String = "",
    val role: String = UserRole.PASSENGER.name,
    val companyId: String? = null,
) {
  fun toDomain(userId: String, favoriteRouteIds: Set<String>): UserProfile {
    return UserProfile(
        id = userId,
        email = email,
        name = name,
        role = UserRole.entries.firstOrNull { it.name == role } ?: UserRole.PASSENGER,
        companyId = companyId,
        favoriteRouteIds = favoriteRouteIds,
    )
  }
}
