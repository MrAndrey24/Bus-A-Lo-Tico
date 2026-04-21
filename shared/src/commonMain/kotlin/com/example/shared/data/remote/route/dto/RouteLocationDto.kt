/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
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
