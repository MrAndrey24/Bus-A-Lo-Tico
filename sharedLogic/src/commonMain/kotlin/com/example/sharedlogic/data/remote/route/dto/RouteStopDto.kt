/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.remote.route.dto

import com.example.sharedlogic.domain.entities.RouteStop
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
