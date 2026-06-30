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
package com.example.sharedlogic.presentation.admin

import com.example.sharedlogic.domain.entities.enums.TripIncidentType

data class AdminRouteIncidentSummary(
    val tripExecutionId: String,
    val incidentId: String,
    val routeName: String,
    val driverName: String,
    val incidentType: TripIncidentType,
    val incidentDetail: String,
    val createdAt: Long,
)
