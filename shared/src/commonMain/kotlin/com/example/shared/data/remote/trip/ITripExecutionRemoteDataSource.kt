/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.trip

import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripIncident

interface ITripExecutionRemoteDataSource {
  suspend fun createTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun getTripExecution(tripExecutionId: String): TripExecution?

  suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution>

  suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident

  suspend fun getIncidents(tripExecutionId: String): List<TripIncident>

  suspend fun deleteIncident(tripExecutionId: String, incidentId: String)
}
