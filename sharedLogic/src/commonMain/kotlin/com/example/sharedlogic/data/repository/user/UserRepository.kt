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

import com.example.sharedlogic.data.local.user.ILocalUserDataSource
import com.example.sharedlogic.data.remote.user.IRemoteUserDataSource
import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val localUserDataSource: ILocalUserDataSource,
    private val remoteUserDataSource: IRemoteUserDataSource,
    private val defaultDispatcher: CoroutineDispatcher,
) : IUserRepository {

  override suspend fun getUserProfile(userId: String): UserProfile? {
    return remoteUserDataSource.getUserProfile(userId)
  }

  override fun getFavoriteRouteIds(userId: String): Flow<Set<String>> = flow {
    // Emit local cached data first
    val cachedFavorites = localUserDataSource.getFavoriteRouteIds(userId)
    emit(cachedFavorites)

    // Fetch remote data & update local cache
    runCatching { remoteUserDataSource.getFavoriteRouteIds(userId) }
        .onSuccess { remoteFavorites ->
          if (remoteFavorites != cachedFavorites) {
            localUserDataSource.clearAndCreateFavorites(userId, remoteFavorites)
            emit(remoteFavorites)
          }
        }
        .onFailure { exception ->
          println(
              "Failed fetching favorites from network. Using local cache. $exception",
          )
        }
  }
      .flowOn(defaultDispatcher)

  override suspend fun getPassengerIdsByFavoriteRoute(routeId: String): Set<String> {
    return remoteUserDataSource.getPassengerIdsByFavoriteRoute(routeId)
  }

  override suspend fun upsertUserProfile(
      userId: String,
      email: String,
      name: String,
      role: UserRole,
      companyId: String?,
  ): UserProfile {
    return remoteUserDataSource.upsertUserProfile(userId, email, name, role, companyId)
  }

  override suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean) {
    // Local-First: Update local database first
    localUserDataSource.setRouteFavorite(userId, routeId, isFavorite)

    // Sync to remote
    runCatching {
      remoteUserDataSource.setRouteFavorite(userId, routeId, isFavorite)
    }
        .onFailure { exception ->
          // Revert local state if remote write fails to stay in sync
          localUserDataSource.setRouteFavorite(userId, routeId, !isFavorite)
          throw exception
        }
  }
}
