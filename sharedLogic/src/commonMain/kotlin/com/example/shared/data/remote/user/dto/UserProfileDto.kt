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
        favoriteRouteIds = favoriteRouteIds,
    )
  }
}
