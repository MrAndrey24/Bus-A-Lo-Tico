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
package com.example.sharedlogic.data.repository.user

import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole

interface IUserRepository {
  suspend fun getUserProfile(userId: String): UserProfile?

  suspend fun getFavoriteRouteIds(userId: String): Set<String>

  suspend fun getPassengerIdsByFavoriteRoute(routeId: String): Set<String>

  suspend fun upsertUserProfile(
      userId: String,
      email: String,
      name: String,
      role: UserRole,
      companyId: String? = null,
  ): UserProfile

  suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean)
}
