/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

data class RouteSchedule(
    val id: String = "",
    val driverId: String,
    val departureTime: String,
    val arrivalTime: String,
)
