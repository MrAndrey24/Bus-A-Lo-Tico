package com.example.shared.domain.entities

data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val companyId: String? = null,
    val favoriteRouteIds: Set<String> = emptySet()
)

enum class UserRole {
    PASSENGER,
    DRIVER,
    ADMIN
}
