package com.example.shared.data.repository.user

import com.example.shared.data.remote.user.IUserRemoteDataSource
import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole

class UserRepository(
    private val remoteDataSource: IUserRemoteDataSource
) : IUserRepository {

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
