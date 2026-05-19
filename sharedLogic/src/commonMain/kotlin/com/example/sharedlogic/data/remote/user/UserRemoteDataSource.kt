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
package com.example.sharedlogic.data.remote.user

import com.example.sharedlogic.data.remote.user.dto.RouteFavoriteDto
import com.example.sharedlogic.data.remote.user.dto.UserProfileDto
import com.example.sharedlogic.domain.entities.UserProfile
import com.example.sharedlogic.domain.entities.UserRole
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class UserRemoteDataSource : IUserRemoteDataSource {

  override suspend fun getUserProfile(userId: String): UserProfile? {
    val userRef = Firebase.firestore.collection(USERS_COLLECTION).document(userId)
    val snapshot = userRef.get()
    if (!snapshot.exists) return null

    val dto = snapshot.data(UserProfileDto.serializer())
    val favoriteRouteIds =
        userRef.collection(FAVORITE_ROUTES_COLLECTION).get().documents.map { it.id }.toSet()

    return dto.toDomain(userId, favoriteRouteIds)
  }

  override suspend fun getPassengerIdsByFavoriteRoute(routeId: String): Set<String> {
    val normalizedRouteId = routeId.trim()
    if (normalizedRouteId.isBlank()) return emptySet()

    val favoritesQuery = Firebase.firestore.collectionGroup(FAVORITE_ROUTES_COLLECTION)
    val favoriteDocuments =
        runCatching { favoritesQuery.where { "routeId" equalTo normalizedRouteId }.get().documents }
            .getOrNull()
            ?: favoritesQuery.get().documents.filter { snapshot ->
              runCatching {
                    snapshot.data(RouteFavoriteDto.serializer()).routeId == normalizedRouteId
                  }
                  .getOrDefault(false)
            }

    val candidateUserIds = favoriteDocuments.mapNotNull { it.reference.parent.parent?.id }.toSet()

    if (candidateUserIds.isEmpty()) return emptySet()

    return candidateUserIds
        .filter { userId ->
          val snapshot =
              runCatching { Firebase.firestore.collection(USERS_COLLECTION).document(userId).get() }
                  .getOrNull() ?: return@filter false

          if (!snapshot.exists) return@filter false
          val role =
              runCatching { snapshot.data(UserProfileDto.serializer()).role }
                  .getOrDefault(UserRole.PASSENGER.name)
          role == UserRole.PASSENGER.name
        }
        .toSet()
  }

  override suspend fun upsertUserProfile(
      userId: String,
      email: String,
      name: String,
      role: UserRole,
      companyId: String?,
  ): UserProfile {
    val userRef = Firebase.firestore.collection(USERS_COLLECTION).document(userId)
    val dto =
        UserProfileDto(
            email = email,
            name = name,
            role = role.name,
            companyId = companyId,
        )
    userRef.set(dto)
    return dto.toDomain(userId, emptySet())
  }

  override suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean) {
    val favoriteRef =
        Firebase.firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(FAVORITE_ROUTES_COLLECTION)
            .document(routeId)

    if (isFavorite) {
      favoriteRef.set(RouteFavoriteDto(routeId = routeId))
    } else {
      favoriteRef.delete()
    }
  }

  private companion object {
    const val USERS_COLLECTION = "users"
    const val FAVORITE_ROUTES_COLLECTION = "favorite_routes"
  }
}
