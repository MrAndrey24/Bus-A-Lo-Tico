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
