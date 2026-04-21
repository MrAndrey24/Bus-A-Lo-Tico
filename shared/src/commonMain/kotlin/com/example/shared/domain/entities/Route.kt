/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

data class Route(
    val id: String,
    val code: String,
    val name: String,
    val companyId: String?,
    val operator: String,
    val startLocation: RouteLocation,
    val endLocation: RouteLocation,
    val distanceKm: Double?,
    val slopeCategory: String?,
    val pavedPercentage: Double?,
    val surveyYear: Int?,
    val fareCrc: Int?,
    val acceptsCardPayment: Boolean,
    val statusText: String,
    val etaText: String,
    val frequencyText: String,
    val isActive: Boolean,
    val isLive: Boolean,
    val isFavorite: Boolean,
    val stops: List<RouteStop>,
    val schedules: List<RouteSchedule>,
    val polylinePoints: List<Coordinate>,
)
