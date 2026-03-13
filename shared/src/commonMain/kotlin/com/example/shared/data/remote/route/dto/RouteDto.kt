package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val code: String = "",
    val name: String = "",
    val companyId: String? = null,
    val operator: String = "",
    val startLocation: RouteLocationDto = RouteLocationDto(),
    val endLocation: RouteLocationDto = RouteLocationDto(),
    val distanceKm: Double? = null,
    val slopeCategory: String? = null,
    val pavedPercentage: Double? = null,
    val surveyYear: Int? = null,
    val statusText: String = "EN TIEMPO REAL",
    val etaText: String = "8 mins",
    val frequencyText: String = "Cada 10 minutos",
    val isActive: Boolean = true,
    val isLive: Boolean = true,
    val isFavorite: Boolean = false,
) {
    fun toDomain(documentId: String, stops: List<RouteStop>, schedules: List<RouteSchedule>): Route {
        return Route(
            id = documentId,
            code = code,
            name = name,
            companyId = companyId,
            operator = operator,
            startLocation = startLocation.toDomain(),
            endLocation = endLocation.toDomain(),
            distanceKm = distanceKm,
            slopeCategory = slopeCategory,
            pavedPercentage = pavedPercentage,
            surveyYear = surveyYear,
            statusText = statusText,
            etaText = etaText,
            frequencyText = frequencyText,
            isActive = isActive,
            isLive = isLive,
            isFavorite = isFavorite,
            stops = stops,
            schedules = schedules
        )
    }
}
