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
