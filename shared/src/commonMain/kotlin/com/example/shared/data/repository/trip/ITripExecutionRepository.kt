package com.example.shared.data.repository.trip

import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripIncident

interface ITripExecutionRepository {
    suspend fun createTripExecution(tripExecution: TripExecution): TripExecution
    suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution
    suspend fun getTripExecution(tripExecutionId: String): TripExecution?
    suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution>
    suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident
    suspend fun getIncidents(tripExecutionId: String): List<TripIncident>
    suspend fun deleteIncident(tripExecutionId: String, incidentId: String)
}
