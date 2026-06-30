/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.domain.entities

import com.example.sharedlogic.domain.entities.enums.TripIncidentType

data class TripIncident(
    val id: String = "",
    val type: TripIncidentType,
    val detail: String,
    val createdAt: Long,
    val location: Coordinate? = null,
)
