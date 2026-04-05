package com.example.shared.data.remote.user.dto

import com.example.shared.domain.entities.UserProfile
import com.example.shared.domain.entities.UserRole
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
            favoriteRouteIds = favoriteRouteIds
        )
    }
}
