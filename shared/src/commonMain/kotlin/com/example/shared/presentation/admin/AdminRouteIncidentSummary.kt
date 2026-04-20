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
