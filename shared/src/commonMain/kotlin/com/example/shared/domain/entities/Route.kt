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
package com.example.shared.domain.entities

data class Route(
    val id: String,
    val code: String,
    val name: String,
    val companyId: String?,
    val operator: String,
    val startLocation: RouteLocation,
    val endLocation: RouteLocation,
    val distanceKm: Double?,
    val slopeCategory: String?,
    val pavedPercentage: Double?,
    val surveyYear: Int?,
    val fareCrc: Int?,
    val acceptsCardPayment: Boolean,
    val statusText: String,
    val etaText: String,
    val frequencyText: String,
    val isActive: Boolean,
    val isLive: Boolean,
    val isFavorite: Boolean,
    val stops: List<RouteStop>,
    val schedules: List<RouteSchedule>,
    val polylinePoints: List<Coordinate>,
)
