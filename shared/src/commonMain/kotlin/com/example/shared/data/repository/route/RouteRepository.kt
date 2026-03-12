package com.example.shared.data.repository.route

import com.example.shared.data.remote.route.IRouteRemoteDataSource
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.Route
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RouteRepository(
    private val remoteDataSource: IRouteRemoteDataSource
) : IRouteRepository {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun getRoutes(): List<Route> = remoteDataSource.getRoutes()
    override suspend fun getRoute(routeId: String): Route? = remoteDataSource.getRoute(routeId)

    // AHORA SÍ: Recibe las paradas y devuelve el trazado de la calle
    override suspend fun getRouteGeometry(stops: List<Coordinate>): List<Coordinate> {
        if (stops.size < 2) return stops

        val waypoints = stops.joinToString(";") { "${it.longitude},${it.latitude}" }

        return try {
            val response: MapboxResponse = httpClient.get("https://api.mapbox.com/directions/v5/mapbox/driving/$waypoints") {
                parameter("access_token", "pk.eyJ1IjoiaXNhYWNjYXN0cm8iLCJhIjoiY21tbXN1Z3ZmMjMybzJvcHh1ZG1hNmwzMiJ9.P9qvlANuZK-xxoAZ98Zujg")
                parameter("geometries", "geojson")
                parameter("overview", "full")
            }.body()

            response.routes.firstOrNull()?.geometry?.coordinates?.map {
                Coordinate(latitude = it[1], longitude = it[0])
            } ?: stops
        } catch (e: Exception) {
            stops // Si falla, devuelve las paradas originales (línea recta)
        }
    }
}

@Serializable
private data class MapboxResponse(val routes: List<MapboxRouteResponse>)
@Serializable
private data class MapboxRouteResponse(val geometry: MapboxGeometryResponse)
@Serializable
private data class MapboxGeometryResponse(val coordinates: List<List<Double>>)