/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.route.dto

import com.example.shared.domain.entities.RouteSchedule
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
