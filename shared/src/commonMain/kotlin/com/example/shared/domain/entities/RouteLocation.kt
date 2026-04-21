/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

data class RouteLocation(
    val province: String? = null,
    val cantonCode: String? = null,
    val canton: String? = null,
    val districtCode: String? = null,
    val district: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
