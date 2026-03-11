package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val code: String = "",
    val name: String = "",
    val statusText: String = "EN TIEMPO REAL",
    val etaText: String = "8 mins",
    val frequencyText: String = "Cada 10 minutos",
    val isActive: Boolean = true,
    val isLive: Boolean = true,
    val isFavorite: Boolean = false,
    val startLatitude: Double = 0.0,
    val startLongitude: Double = 0.0,
    val endLatitude: Double = 0.0,
    val endLongitude: Double = 0.0
) {
    fun toDomain(documentId: String, stops: List<RouteStop>, schedules: List<RouteSchedule>): Route {
        return Route(
            id = documentId,
            code = code,
            name = name,
            statusText = statusText,
            etaText = etaText,
            frequencyText = frequencyText,
            isActive = isActive,
            isLive = isLive,
            isFavorite = isFavorite,
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            endLatitude = endLatitude,
            endLongitude = endLongitude,
            stops = stops,
            schedules = schedules
        )
    }
}
