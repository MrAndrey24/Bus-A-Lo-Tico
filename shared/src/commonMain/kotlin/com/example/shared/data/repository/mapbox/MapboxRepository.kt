package com.example.shared.data.repository.mapbox

import AppSecrets
import com.example.shared.data.repository.mapbox.MapboxConstants.DIRECTIONS_BASE_URL
import com.example.shared.data.repository.mapbox.MapboxConstants.GEOCODING_BASE_URL
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_ACCESS_TOKEN
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_COUNTRY
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_GEOMETRIES
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_LIMIT
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_OVERVIEW
import com.example.shared.data.repository.mapbox.MapboxConstants.PARAM_PROXIMITY
import com.example.shared.data.repository.mapbox.MapboxConstants.VALUE_COUNTRY_CR
import com.example.shared.data.repository.mapbox.MapboxConstants.VALUE_FULL
import com.example.shared.data.repository.mapbox.MapboxConstants.VALUE_GEOJSON
import com.example.shared.data.repository.mapbox.MapboxConstants.VALUE_PROXIMITY_SJ
import com.example.shared.data.repository.mapbox.MapboxConstants.VALUE_SEARCH_LIMIT
import com.example.shared.domain.entities.Coordinate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodeURLPath
import kotlinx.serialization.Serializable

class MapboxRepository(private val httpClient: HttpClient) : IMapboxRepository {

    private val accessToken = AppSecrets.TOKEN

    override suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate> {
        if (waypoints.size < 2) return waypoints

        val points = waypoints.joinToString(";") { "${it.longitude},${it.latitude}" }

        return try {
            val response: MapboxResponse = httpClient
                .get("$DIRECTIONS_BASE_URL$points") {
                    parameter(PARAM_ACCESS_TOKEN, accessToken)
                    parameter(PARAM_GEOMETRIES, VALUE_GEOJSON)
                    parameter(PARAM_OVERVIEW, VALUE_FULL)
                }.body()

            val coordinates = response.routes.firstOrNull()?.geometry?.coordinates
            
            coordinates?.map { Coordinate(latitude = it[1], longitude = it[0]) } ?: waypoints
        } catch (e: Exception) {
            waypoints
        }
    }

    override suspend fun getStopDurations(waypoints: List<Coordinate>): List<Double> {
        if (waypoints.size < 2) return emptyList()

        val points = waypoints.joinToString(";") { "${it.longitude},${it.latitude}" }

        return try {
            val response: MapboxResponse = httpClient
                .get("$DIRECTIONS_BASE_URL$points") {
                    parameter(PARAM_ACCESS_TOKEN, accessToken)
                    parameter(PARAM_GEOMETRIES, VALUE_GEOJSON)
                    parameter(PARAM_OVERVIEW, VALUE_FULL)
                }.body()

            response.routes.firstOrNull()?.legs?.map { it.duration } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchLocation(query: String): List<Coordinate> {
        if (query.isBlank()) return emptyList()

        return try {
            val encodedQuery = query.encodeURLPath()
            val response: MapboxGeocodingResponse = httpClient
                .get("$GEOCODING_BASE_URL$encodedQuery.json") {
                    parameter(PARAM_ACCESS_TOKEN, accessToken)
                    parameter(PARAM_LIMIT, VALUE_SEARCH_LIMIT)
                    parameter(PARAM_COUNTRY, VALUE_COUNTRY_CR)
                    parameter(PARAM_PROXIMITY, VALUE_PROXIMITY_SJ)
                }.body()

            response.features.map { Coordinate(latitude = it.center[1], longitude = it.center[0]) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

@Serializable 
private data class MapboxResponse(val routes: List<MapboxRouteResponse>)

@Serializable
private data class MapboxRouteResponse(
    val geometry: MapboxGeometryResponse,
    val legs: List<MapboxLegResponse> = emptyList(),
)

@Serializable 
private data class MapboxGeometryResponse(val coordinates: List<List<Double>>)

@Serializable
private data class MapboxLegResponse(val duration: Double)

@Serializable
private data class MapboxGeocodingResponse(val features: List<MapboxFeatureResponse>)

@Serializable
private data class MapboxFeatureResponse(val center: List<Double>)
