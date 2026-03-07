package com.example.shared.domain.entities

data class Route(
    val id: String,
    val name: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val stops: List<RouteStop>,
    val schedules: List<RouteSchedule>
)

data class RouteStop(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val order: Int
)

data class RouteSchedule(
    val id: String,
    val driverId: String,
    val departureTime: String,
    val arrivalTime: String
)
