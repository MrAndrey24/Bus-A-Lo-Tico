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
package com.example.shared.data.repository.trip

import com.example.shared.data.remote.trip.ITripExecutionRemoteDataSource
import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripIncident

class TripExecutionRepository(
    private val remoteDataSource: ITripExecutionRemoteDataSource,
) : ITripExecutionRepository {

  override suspend fun createTripExecution(tripExecution: TripExecution): TripExecution {
    return remoteDataSource.createTripExecution(tripExecution)
  }

  override suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution {
    return remoteDataSource.updateTripExecution(tripExecution)
  }

  override suspend fun getTripExecution(tripExecutionId: String): TripExecution? {
    return remoteDataSource.getTripExecution(tripExecutionId)
  }

  override suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution> {
    return remoteDataSource.getTripExecutionsByDriver(driverId)
  }

  override suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident {
    return remoteDataSource.addIncident(tripExecutionId, incident)
  }

  override suspend fun getIncidents(tripExecutionId: String): List<TripIncident> {
    return remoteDataSource.getIncidents(tripExecutionId)
  }

  override suspend fun deleteIncident(tripExecutionId: String, incidentId: String) {
    remoteDataSource.deleteIncident(tripExecutionId, incidentId)
  }
}
