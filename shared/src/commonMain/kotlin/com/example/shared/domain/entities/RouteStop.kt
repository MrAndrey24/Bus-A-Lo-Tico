package com.example.shared.domain.entities

data class RouteStop(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val order: Int
)
