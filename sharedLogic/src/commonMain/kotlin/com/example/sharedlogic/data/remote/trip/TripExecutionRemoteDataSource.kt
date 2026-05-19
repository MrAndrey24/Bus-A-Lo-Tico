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
package com.example.sharedlogic.data.remote.trip

import com.example.sharedlogic.data.remote.trip.dto.TripExecutionDto
import com.example.sharedlogic.data.remote.trip.dto.TripIncidentDto
import com.example.sharedlogic.domain.entities.TripExecution
import com.example.sharedlogic.domain.entities.TripIncident
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
