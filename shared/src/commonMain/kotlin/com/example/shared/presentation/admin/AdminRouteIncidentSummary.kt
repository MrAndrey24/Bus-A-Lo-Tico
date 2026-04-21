/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.admin

import com.example.shared.domain.entities.TripIncidentType

data class AdminRouteIncidentSummary(
    val tripExecutionId: String,
    val incidentId: String,
    val routeName: String,
    val driverName: String,
    val incidentType: TripIncidentType,
    val incidentDetail: String,
    val createdAt: Long,
)
