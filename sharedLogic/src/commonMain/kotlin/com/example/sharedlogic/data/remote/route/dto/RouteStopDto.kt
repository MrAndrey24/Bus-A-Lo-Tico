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
