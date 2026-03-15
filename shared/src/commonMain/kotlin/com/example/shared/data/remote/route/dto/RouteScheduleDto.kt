package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.RouteSchedule
import kotlinx.serialization.Serializable

@Serializable
data class RouteScheduleDto(
    val driverId: String = "",
    val departureTime: String = "",
    val arrivalTime: String = ""
) {
    fun toDomain(documentId: String): RouteSchedule {
        return RouteSchedule(
            id = documentId,
            driverId = driverId,
            departureTime = departureTime,
            arrivalTime = arrivalTime
        )
    }
}
