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

import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
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
    val fareCrc: Int? = null,
    val acceptsCardPayment: Boolean = false,
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
        fareCrc = fareCrc,
        acceptsCardPayment = acceptsCardPayment,
        statusText = statusText,
        etaText = etaText,
        frequencyText = frequencyText,
        isActive = isActive,
        isLive = isLive,
        isFavorite = isFavorite,
        stops = stops,
        schedules = schedules,
        polylinePoints = emptyList(),
    )
  }

  companion object {
    fun fromDomain(route: Route): RouteDto {
      return RouteDto(
          code = route.code,
          name = route.name,
          companyId = route.companyId,
          operator = route.operator,
          startLocation = RouteLocationDto.fromDomain(route.startLocation),
          endLocation = RouteLocationDto.fromDomain(route.endLocation),
          distanceKm = route.distanceKm,
          slopeCategory = route.slopeCategory,
          pavedPercentage = route.pavedPercentage,
          surveyYear = route.surveyYear,
          fareCrc = route.fareCrc,
          acceptsCardPayment = route.acceptsCardPayment,
          statusText = route.statusText,
          etaText = route.etaText,
          frequencyText = route.frequencyText,
          isActive = route.isActive,
          isLive = route.isLive,
          isFavorite = route.isFavorite,
      )
    }
  }
}
