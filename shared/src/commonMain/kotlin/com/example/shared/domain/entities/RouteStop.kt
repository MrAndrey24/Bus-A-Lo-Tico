/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

data class RouteStop(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val order: Int,
)
