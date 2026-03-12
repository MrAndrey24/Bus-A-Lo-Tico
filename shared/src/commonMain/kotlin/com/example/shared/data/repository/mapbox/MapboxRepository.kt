package com.example.shared.data.repository.mapbox

import com.example.shared.domain.entities.Coordinate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

class MapboxRepository(
    private val httpClient: HttpClient
) : IMapboxRepository {

    private val accessToken = "pk.eyJ1IjoiaXNhYWNjYXN0cm8iLCJhIjoiY21tbXN1Z3ZmMjMybzJvcHh1ZG1hNmwzMiJ9.P9qvlANuZK-xxoAZ98Zujg"

    override suspend fun getStreetGeometry(waypoints: List<Coordinate>): List<Coordinate> {
        if (waypoints.size < 2) return waypoints

        // Formateamos los puntos para Mapbox: "long,lat;long,lat"
        val points = waypoints.joinToString(";") { "${it.longitude},${it.latitude}" }

        return try {
            val response: MapboxResponse = httpClient.get("https://api.mapbox.com/directions/v5/mapbox/driving/$points") {
                parameter("access_token", accessToken)
                parameter("geometries", "geojson")
                parameter("overview", "full")
            }.body()

            // Mapbox nos devuelve una lista de coordenadas [long, lat]
            response.routes.firstOrNull()?.geometry?.coordinates?.map { coord ->
                Coordinate(latitude = coord[1], longitude = coord[0])
            } ?: waypoints
        } catch (e: Exception) {
            // Si algo falla (internet o token), devolvemos los puntos originales para no romper el mapa
            waypoints
        }
    }
}

// Modelos de respuesta internos (Solo para este archivo)
@Serializable
private data class MapboxResponse(val routes: List<MapboxRouteResponse>)

@Serializable
private data class MapboxRouteResponse(val geometry: MapboxGeometryResponse)

@Serializable
private data class MapboxGeometryResponse(val coordinates: List<List<Double>>)