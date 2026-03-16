package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.RouteStop
import kotlinx.serialization.Serializable

@Serializable
data class RouteStopDto(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val order: Int = 0
) {
    fun toDomain(documentId: String): RouteStop {
        return RouteStop(
            id = documentId,
            name = name,
            latitude = latitude,
            longitude = longitude,
            order = order
        )
    }
}
