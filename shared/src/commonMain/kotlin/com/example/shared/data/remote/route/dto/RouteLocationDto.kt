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
package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.RouteLocation
import kotlinx.serialization.Serializable

@Serializable
data class RouteLocationDto(
    val province: String? = null,
    val cantonCode: String? = null,
    val canton: String? = null,
    val districtCode: String? = null,
    val district: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
) {
  fun toDomain(): RouteLocation {
    return RouteLocation(
        province = province?.trim().takeUnless { it.isNullOrBlank() },
        cantonCode = cantonCode?.trim().takeUnless { it.isNullOrBlank() },
        canton = canton?.trim().takeUnless { it.isNullOrBlank() },
        districtCode = districtCode?.trim().takeUnless { it.isNullOrBlank() },
        district = district?.trim().takeUnless { it.isNullOrBlank() },
        latitude = latitude,
        longitude = longitude,
    )
  }

  companion object {
    fun fromDomain(location: RouteLocation): RouteLocationDto {
      return RouteLocationDto(
          province = location.province,
          cantonCode = location.cantonCode,
          canton = location.canton,
          districtCode = location.districtCode,
          district = location.district,
          latitude = location.latitude,
          longitude = location.longitude,
      )
    }
  }
}
