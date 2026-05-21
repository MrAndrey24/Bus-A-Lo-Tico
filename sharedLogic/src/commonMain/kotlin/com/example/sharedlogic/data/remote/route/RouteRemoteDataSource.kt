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
package com.example.sharedlogic.data.remote.route

import com.example.sharedlogic.data.remote.route.dto.RouteDto
import com.example.sharedlogic.data.remote.route.dto.RouteScheduleDto
import com.example.sharedlogic.data.remote.route.dto.RouteStopDto
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.time.Clock

class RouteRemoteDataSource : IRouteRemoteDataSource {

  override suspend fun getRoutes(): List<Route> {
    return Firebase.firestore.collection(ROUTES_COLLECTION).get().documents.map { routeSnapshot ->
      val routeDto = routeSnapshot.data(RouteDto.serializer())
      routeDto.toDomain(documentId = routeSnapshot.id, stops = emptyList(), schedules = emptyList())
    }
  }

  override suspend fun getRoute(routeId: String): Route? {
    val routeRef = Firebase.firestore.collection(ROUTES_COLLECTION).document(routeId)

    val routeSnapshot = routeRef.get()
    if (!routeSnapshot.exists) return null

    val routeDto = routeSnapshot.data(RouteDto.serializer())

    val stops =
        routeRef
            .collection(STOPS_COLLECTION)
            .get()
            .documents
            .map { stopSnapshot ->
              val stopDto = stopSnapshot.data(RouteStopDto.serializer())
              stopDto.toDomain(stopSnapshot.id)
            }
            .sortedBy { it.order }

    val schedules =
        routeRef.collection(SCHEDULES_COLLECTION).get().documents.map { scheduleSnapshot ->
          val scheduleDto = scheduleSnapshot.data(RouteScheduleDto.serializer())
          scheduleDto.toDomain(scheduleSnapshot.id)
        }

    return routeDto.toDomain(documentId = routeSnapshot.id, stops = stops, schedules = schedules)
  }

  override suspend fun getRouteByDriverId(driverId: String): Route? {
    return getRoutesByDriverId(driverId).firstOrNull()
  }

  override suspend fun getRoutesByDriverId(driverId: String): List<Route> {
    val schedulesQuery = Firebase.firestore.collectionGroup(SCHEDULES_COLLECTION)

    val scheduleDocuments =
        runCatching { schedulesQuery.where { "driverId" equalTo driverId }.get().documents }
            .getOrNull()
            ?: schedulesQuery.get().documents.filter { snapshot ->
              val scheduleDto = snapshot.data(RouteScheduleDto.serializer())
              scheduleDto.driverId == driverId
            }

    val routeIds = scheduleDocuments.mapNotNull { it.reference.parent.parent?.id }.distinct()

    return routeIds.mapNotNull { routeId -> getRoute(routeId) }
  }

  override suspend fun upsertRoute(route: Route): Route {
    val routeId = route.id.ifBlank { generateDocumentId("route") }
    Firebase.firestore
        .collection(ROUTES_COLLECTION)
        .document(routeId)
        .set(RouteDto.fromDomain(route))
    return route.copy(id = routeId)
  }

  override suspend fun upsertRouteStop(routeId: String, stop: RouteStop): RouteStop {
    val stopsCollection =
        Firebase.firestore
            .collection(ROUTES_COLLECTION)
            .document(routeId)
            .collection(STOPS_COLLECTION)

    val stopId = stop.id.ifBlank { generateDocumentId("stop") }
    stopsCollection.document(stopId).set(RouteStopDto.fromDomain(stop))
    return stop.copy(id = stopId)
  }

  override suspend fun deleteRouteStop(routeId: String, stopId: String) {
    Firebase.firestore
        .collection(ROUTES_COLLECTION)
        .document(routeId)
        .collection(STOPS_COLLECTION)
        .document(stopId)
        .delete()
  }

  override suspend fun upsertRouteSchedule(
      routeId: String,
      schedule: RouteSchedule,
  ): RouteSchedule {
    val schedulesCollection =
        Firebase.firestore
            .collection(ROUTES_COLLECTION)
            .document(routeId)
            .collection(SCHEDULES_COLLECTION)

    val scheduleId = schedule.id.ifBlank { generateDocumentId("schedule") }
    schedulesCollection.document(scheduleId).set(RouteScheduleDto.fromDomain(schedule))
    return schedule.copy(id = scheduleId)
  }

  override suspend fun deleteRouteSchedule(routeId: String, scheduleId: String) {
    Firebase.firestore
        .collection(ROUTES_COLLECTION)
        .document(routeId)
        .collection(SCHEDULES_COLLECTION)
        .document(scheduleId)
        .delete()
  }

  private companion object {
    const val ROUTES_COLLECTION = "routes"
    const val STOPS_COLLECTION = "stops"
    const val SCHEDULES_COLLECTION = "schedules"
  }

  private fun generateDocumentId(prefix: String): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val random = Random.nextInt(100000, 999999)
    return "${prefix}_${now}_$random"
  }
}
