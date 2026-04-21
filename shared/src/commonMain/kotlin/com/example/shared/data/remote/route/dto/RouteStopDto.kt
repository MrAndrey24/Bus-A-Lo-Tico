/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.RouteStop
import kotlinx.serialization.Serializable

@Serializable
data class RouteStopDto(
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val order: Int = 0,
) {
  fun toDomain(documentId: String): RouteStop {
    return RouteStop(
        id = documentId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        order = order,
    )
  }

  companion object {
    fun fromDomain(stop: RouteStop): RouteStopDto {
      return RouteStopDto(
          name = stop.name,
          latitude = stop.latitude,
          longitude = stop.longitude,
          order = stop.order,
      )
    }
  }
}
