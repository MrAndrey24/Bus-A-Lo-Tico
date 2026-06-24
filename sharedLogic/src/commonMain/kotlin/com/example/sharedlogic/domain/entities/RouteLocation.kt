/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.domain.entities

data class RouteLocation(
    val province: String? = null,
    val cantonCode: String? = null,
    val canton: String? = null,
    val districtCode: String? = null,
    val district: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
