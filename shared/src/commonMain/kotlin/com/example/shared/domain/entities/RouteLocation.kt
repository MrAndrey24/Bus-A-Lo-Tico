package com.example.shared.domain.entities

data class RouteLocation(
    val province: String? = null,
    val cantonCode: String? = null,
    val canton: String? = null,
    val districtCode: String? = null,
    val district: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
