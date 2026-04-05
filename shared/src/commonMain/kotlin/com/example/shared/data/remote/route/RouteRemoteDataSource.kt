package com.example.shared.data.remote.route

import com.example.shared.data.remote.route.dto.RouteDto
import com.example.shared.data.remote.route.dto.RouteScheduleDto
import com.example.shared.data.remote.route.dto.RouteStopDto
import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlin.random.Random
import kotlin.time.Clock

class RouteRemoteDataSource : IRouteRemoteDataSource {

    override suspend fun getRoutes(): List<Route> {
        return Firebase.firestore
            .collection(ROUTES_COLLECTION)
            .get()
            .documents
            .map { routeSnapshot ->
                val routeDto = routeSnapshot.data(RouteDto.serializer())
                routeDto.toDomain(
                    documentId = routeSnapshot.id,
                    stops = emptyList(),
                    schedules = emptyList()
                )
            }
    }

    override suspend fun getRoute(routeId: String): Route? {
        val routeRef = Firebase.firestore
            .collection(ROUTES_COLLECTION)
            .document(routeId)

        val routeSnapshot = routeRef.get()
        if (!routeSnapshot.exists) return null

        val routeDto = routeSnapshot.data(RouteDto.serializer())

        val stops = routeRef
            .collection(STOPS_COLLECTION)
            .get()
            .documents
            .map { stopSnapshot ->
                val stopDto = stopSnapshot.data(RouteStopDto.serializer())
                stopDto.toDomain(stopSnapshot.id)
            }
            .sortedBy { it.order }

        val schedules = routeRef
            .collection(SCHEDULES_COLLECTION)
            .get()
            .documents
            .map { scheduleSnapshot ->
                val scheduleDto = scheduleSnapshot.data(RouteScheduleDto.serializer())
                scheduleDto.toDomain(scheduleSnapshot.id)
            }

        return routeDto.toDomain(
            documentId = routeSnapshot.id,
            stops = stops,
            schedules = schedules
        )
    }

    override suspend fun getRouteByDriverId(driverId: String): Route? {
        val schedulesQuery = Firebase.firestore.collectionGroup(SCHEDULES_COLLECTION)

        val scheduleSnapshot = runCatching {
            schedulesQuery
                .where { "driverId" equalTo driverId }
                .limit(1)
                .get()
                .documents
                .firstOrNull()
        }.getOrNull() ?: schedulesQuery
            .get()
            .documents
            .firstOrNull { snapshot ->
                val scheduleDto = snapshot.data(RouteScheduleDto.serializer())
                scheduleDto.driverId == driverId
            }
            ?: return null

        val routeId = scheduleSnapshot.reference.parent.parent?.id ?: return null
        return getRoute(routeId)
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
        val stopsCollection = Firebase.firestore
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

    override suspend fun upsertRouteSchedule(routeId: String, schedule: RouteSchedule): RouteSchedule {
        val schedulesCollection = Firebase.firestore
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
