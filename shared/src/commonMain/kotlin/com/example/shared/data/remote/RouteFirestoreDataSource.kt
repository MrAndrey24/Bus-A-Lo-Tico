package com.example.shared.data.remote

import com.example.shared.domain.entities.Route
import com.example.shared.domain.entities.RouteSchedule
import com.example.shared.domain.entities.RouteStop
import com.example.shared.domain.repository.IRouteRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

class RouteFirestoreDataSource : IRouteRepository {

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

    @Serializable
    private data class RouteDto(
        val name: String = "",
        val startLatitude: Double = 0.0,
        val startLongitude: Double = 0.0,
        val endLatitude: Double = 0.0,
        val endLongitude: Double = 0.0
    ) {
        fun toDomain(documentId: String, stops: List<RouteStop>, schedules: List<RouteSchedule>): Route {
            return Route(
                id = documentId,
                name = name,
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude,
                stops = stops,
                schedules = schedules
            )
        }
    }

    @Serializable
    private data class RouteStopDto(
        val name: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val order: Int = 0
    ) {
        fun toDomain(documentId: String): RouteStop {
            return RouteStop(
                id = documentId,
                name = name,
                latitude = latitude,
                longitude = longitude,
                order = order
            )
        }
    }

    @Serializable
    private data class RouteScheduleDto(
        val driverId: String = "",
        val departureTime: String = "",
        val arrivalTime: String = ""
    ) {
        fun toDomain(documentId: String): RouteSchedule {
            return RouteSchedule(
                id = documentId,
                driverId = driverId,
                departureTime = departureTime,
                arrivalTime = arrivalTime
            )
        }
    }

    private companion object {
        const val ROUTES_COLLECTION = "routes"
        const val STOPS_COLLECTION = "stops"
        const val SCHEDULES_COLLECTION = "schedules"
    }
}
