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
    val statusText: String,
    val etaText: String,
    val frequencyText: String,
    val isActive: Boolean,
    val isLive: Boolean,
    val isFavorite: Boolean,
    val stops: List<RouteStop>,
    val schedules: List<RouteSchedule>,
    val polylinePoints: List<Coordinate>
)
