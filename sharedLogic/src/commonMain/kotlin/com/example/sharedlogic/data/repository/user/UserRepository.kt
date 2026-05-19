/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
