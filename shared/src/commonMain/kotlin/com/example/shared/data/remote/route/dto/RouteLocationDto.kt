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
    val longitude: Double? = null
) {
    fun toDomain(): RouteLocation {
        return RouteLocation(
            province = province?.trim().takeUnless { it.isNullOrBlank() },
            cantonCode = cantonCode?.trim().takeUnless { it.isNullOrBlank() },
            canton = canton?.trim().takeUnless { it.isNullOrBlank() },
            districtCode = districtCode?.trim().takeUnless { it.isNullOrBlank() },
            district = district?.trim().takeUnless { it.isNullOrBlank() },
            latitude = latitude,
            longitude = longitude
        )
    }
}
