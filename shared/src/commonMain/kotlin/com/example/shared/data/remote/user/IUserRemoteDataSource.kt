package com.example.shared.data.remote.user

import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole

interface IUserRemoteDataSource {
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun getSubscribedPassengerIdsForRoute(routeId: String): List<String>
    suspend fun upsertUserProfile(
        userId: String,
        email: String,
        name: String,
        role: UserRole,
        companyId: String? = null,
    ): UserProfile
    suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean)
}
