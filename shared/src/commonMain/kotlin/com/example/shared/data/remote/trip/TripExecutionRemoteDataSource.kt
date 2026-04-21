/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.trip

import com.example.shared.data.remote.trip.dto.TripExecutionDto
import com.example.shared.data.remote.trip.dto.TripIncidentDto
import com.example.shared.domain.entities.TripExecution
import com.example.shared.domain.entities.TripIncident
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.time.Clock

class TripExecutionRemoteDataSource : ITripExecutionRemoteDataSource {

  override suspend fun createTripExecution(tripExecution: TripExecution): TripExecution {
    val tripRef =
        if (tripExecution.id.isBlank()) {
          Firebase.firestore
              .collection(TRIP_EXECUTIONS_COLLECTION)
              .document(generateDocumentId("trip"))
        } else {
          Firebase.firestore.collection(TRIP_EXECUTIONS_COLLECTION).document(tripExecution.id)
        }

    tripRef.set(TripExecutionDto.fromDomain(tripExecution))
    return tripExecution.copy(id = tripRef.id)
  }

  override suspend fun updateTripExecution(tripExecution: TripExecution): TripExecution {
    val tripRef =
        Firebase.firestore.collection(TRIP_EXECUTIONS_COLLECTION).document(tripExecution.id)

    tripRef.set(TripExecutionDto.fromDomain(tripExecution))
    return tripExecution
  }

  override suspend fun getTripExecution(tripExecutionId: String): TripExecution? {
    val snapshot =
        Firebase.firestore.collection(TRIP_EXECUTIONS_COLLECTION).document(tripExecutionId).get()

    if (!snapshot.exists) return null

    return snapshot.data(TripExecutionDto.serializer()).toDomain(snapshot.id)
  }

  override suspend fun getTripExecutionsByDriver(driverId: String): List<TripExecution> {
    return Firebase.firestore
        .collection(TRIP_EXECUTIONS_COLLECTION)
        .get()
        .documents
        .map { snapshot -> snapshot.data(TripExecutionDto.serializer()).toDomain(snapshot.id) }
        .filter { it.driverId == driverId }
        .sortedByDescending { it.startedAt }
  }

  override suspend fun addIncident(tripExecutionId: String, incident: TripIncident): TripIncident {
    val incidentCollection =
        Firebase.firestore
            .collection(TRIP_EXECUTIONS_COLLECTION)
            .document(tripExecutionId)
            .collection(INCIDENTS_COLLECTION)

    val incidentRef =
        if (incident.id.isBlank()) {
          incidentCollection.document(generateDocumentId("incident"))
        } else {
          incidentCollection.document(incident.id)
        }

    incidentRef.set(TripIncidentDto.fromDomain(incident))

    getTripExecution(tripExecutionId)?.let { trip ->
      if (!trip.hadIncidents) {
        updateTripExecution(trip.copy(hadIncidents = true))
      }
    }

    return incident.copy(id = incidentRef.id)
  }

  override suspend fun getIncidents(tripExecutionId: String): List<TripIncident> {
    return Firebase.firestore
        .collection(TRIP_EXECUTIONS_COLLECTION)
        .document(tripExecutionId)
        .collection(INCIDENTS_COLLECTION)
        .get()
        .documents
        .map { snapshot -> snapshot.data(TripIncidentDto.serializer()).toDomain(snapshot.id) }
        .sortedByDescending { it.createdAt }
  }

  override suspend fun deleteIncident(tripExecutionId: String, incidentId: String) {
    Firebase.firestore
        .collection(TRIP_EXECUTIONS_COLLECTION)
        .document(tripExecutionId)
        .collection(INCIDENTS_COLLECTION)
        .document(incidentId)
        .delete()
  }

  private companion object {
    const val TRIP_EXECUTIONS_COLLECTION = "tripExecutions"
    const val INCIDENTS_COLLECTION = "incidents"
  }

  private fun generateDocumentId(prefix: String): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val random = Random.nextInt(100000, 999999)
    return "${prefix}_${now}_$random"
  }
}
