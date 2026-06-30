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

import com.example.sharedlogic.domain.entities.RouteSchedule
import kotlinx.serialization.Serializable

@Serializable
data class RouteScheduleDto(
    val driverId: String = "",
    val departureTime: String = "",
    val arrivalTime: String = "",
) {
  fun toDomain(documentId: String): RouteSchedule {
    return RouteSchedule(
        id = documentId,
        driverId = driverId,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
    )
  }

  companion object {
    fun fromDomain(schedule: RouteSchedule): RouteScheduleDto {
      return RouteScheduleDto(
          driverId = schedule.driverId,
          departureTime = schedule.departureTime,
          arrivalTime = schedule.arrivalTime,
      )
    }
  }
}
