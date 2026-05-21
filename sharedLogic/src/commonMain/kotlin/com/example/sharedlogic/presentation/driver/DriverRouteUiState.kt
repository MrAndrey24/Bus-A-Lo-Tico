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
package com.example.sharedlogic.presentation.driver

import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.TripExecution

data class DriverRouteUiState(
    val route: Route? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isStartingTrip: Boolean = false,
    val isEndingTrip: Boolean = false,
    val isResumingTrip: Boolean = false,
    val isTransferringTrip: Boolean = false,
    val isReportingIncident: Boolean = false,
    val activeTripExecutionId: String? = null,
    val currentTripExecution: TripExecution? = null,
    val completedTodayScheduleIds: Set<String> = emptySet(),
    val tripHistory: List<DriverTripHistoryRecord> = emptyList(),
    val transferDrivers: List<Driver> = emptyList(),
)
