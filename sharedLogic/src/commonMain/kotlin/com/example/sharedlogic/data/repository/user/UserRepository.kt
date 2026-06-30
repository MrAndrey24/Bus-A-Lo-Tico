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
package com.example.sharedlogic.data.repository.user

import com.example.sharedlogic.data.remote.user.IUserRemoteDataSource
import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole

class UserRepository(private val remoteDataSource: IUserRemoteDataSource) : IUserRepository {

  override suspend fun getUserProfile(userId: String): UserProfile? {
    return remoteDataSource.getUserProfile(userId)
  }

  override suspend fun getFavoriteRouteIds(userId: String): Set<String> {
    return remoteDataSource.getUserProfile(userId)?.favoriteRouteIds ?: emptySet()
  }

  override suspend fun getPassengerIdsByFavoriteRoute(routeId: String): Set<String> {
    return remoteDataSource.getPassengerIdsByFavoriteRoute(routeId)
  }

  override suspend fun upsertUserProfile(
      userId: String,
      email: String,
      name: String,
      role: UserRole,
      companyId: String?,
  ): UserProfile {
    return remoteDataSource.upsertUserProfile(userId, email, name, role, companyId)
  }

  override suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean) {
    remoteDataSource.setRouteFavorite(userId, routeId, isFavorite)
  }
}
