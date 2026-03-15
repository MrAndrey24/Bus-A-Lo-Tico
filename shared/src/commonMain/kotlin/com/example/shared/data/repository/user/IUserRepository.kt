package com.example.shared.data.repository.user

import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole

interface IUserRepository {
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun getFavoriteRouteIds(userId: String): Set<String>
    suspend fun upsertUserProfile(userId: String, email: String, name: String, role: UserRole): UserProfile
    suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean)
}
