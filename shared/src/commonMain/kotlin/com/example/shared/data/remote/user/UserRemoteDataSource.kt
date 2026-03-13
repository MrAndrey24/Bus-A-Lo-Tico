package com.example.shared.data.remote.user

import com.example.shared.data.remote.user.dto.RouteFavoriteDto
import com.example.shared.data.remote.user.dto.UserProfileDto
import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class UserRemoteDataSource : IUserRemoteDataSource {

    override suspend fun getUserProfile(userId: String): UserProfile? {
        val userRef = Firebase.firestore.collection(USERS_COLLECTION).document(userId)
        val snapshot = userRef.get()
        if (!snapshot.exists) return null

        val dto = snapshot.data(UserProfileDto.serializer())
        val favoriteRouteIds = userRef
            .collection(FAVORITE_ROUTES_COLLECTION)
            .get()
            .documents
            .map { it.id }
            .toSet()

        return dto.toDomain(userId, favoriteRouteIds)
    }

    override suspend fun upsertUserProfile(
        userId: String,
        email: String,
        name: String,
        role: UserRole
    ): UserProfile {
        val userRef = Firebase.firestore.collection(USERS_COLLECTION).document(userId)
        val dto = UserProfileDto(
            email = email,
            name = name,
            role = role.name
        )
        userRef.set(dto)
        return dto.toDomain(userId, emptySet())
    }

    override suspend fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean) {
        val favoriteRef = Firebase.firestore
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
