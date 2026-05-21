/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
