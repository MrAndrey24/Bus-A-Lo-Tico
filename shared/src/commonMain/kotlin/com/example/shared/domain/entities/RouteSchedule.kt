package com.example.shared.domain.entities

data class RouteSchedule(
    val id: String = "",
    val driverId: String,
    val departureTime: String,
    val arrivalTime: String
)
