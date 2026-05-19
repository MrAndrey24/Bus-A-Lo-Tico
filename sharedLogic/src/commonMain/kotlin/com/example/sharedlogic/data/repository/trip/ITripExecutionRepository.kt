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
package com.example.sharedlogic.data.repository.trip

import com.example.sharedlogic.domain.entities.TripExecution
import com.example.sharedlogic.domain.entities.TripIncident

interface ITripExecutionRepository {
  suspend fun createTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun getTripExecution(tripExecutionId: String): TripExecution?

  suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution>

  suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident

  suspend fun getIncidents(tripExecutionId: String): List<TripIncident>

  suspend fun deleteIncident(tripExecutionId: String, incidentId: String)
}
