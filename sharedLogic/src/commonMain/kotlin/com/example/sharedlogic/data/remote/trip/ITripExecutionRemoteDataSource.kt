/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.data.remote.trip

import com.example.sharedlogic.domain.entities.TripExecution
import com.example.sharedlogic.domain.entities.TripIncident

interface ITripExecutionRemoteDataSource {
  suspend fun createTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution

  suspend fun getTripExecution(tripExecutionId: String): TripExecution?

  suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution>

  suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident

  suspend fun getIncidents(tripExecutionId: String): List<TripIncident>

  suspend fun deleteIncident(tripExecutionId: String, incidentId: String)
}
