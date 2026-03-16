package com.example.shared.data.repository.mapbox
import com.example.shared.domain.entities.Coordinate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class MapboxRepository(
    private val httpClient: HttpClient
) : IMapboxRepository {

    private val accessToken = AppSecrets.TOKEN

    override suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate> {
        if (waypoints.size < 2) return waypoints

        val points = waypoints.joinToString(";") { "${it.longitude},${it.latitude}" }

        return try {
            val response: MapboxResponse = httpClient.get("https://api.mapbox.com/directions/v5/mapbox/driving/$points") {
                parameter("access_token", accessToken)
                parameter("geometries", "geojson")
                parameter("overview", "full")
            }.body()

            response.routes.firstOrNull()?.geometry?.coordinates?.map { coord ->
                Coordinate(latitude = coord[1], longitude = coord[0])
            } ?: waypoints
        } catch (e: Exception) {
            waypoints
        }
    }
}

@Serializable
private data class MapboxResponse(val routes: List<MapboxRouteResponse>)

@Serializable
private data class MapboxRouteResponse(val geometry: MapboxGeometryResponse)

@Serializable
private data class MapboxGeometryResponse(val coordinates: List<List<Double>>)