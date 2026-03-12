package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
actual fun MapViewComponent(
    modifier: Modifier,
    coordinate: Coordinate,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop>,
    polylinePoints: List<Coordinate>
) {
    val currentPoint = Point.fromLngLat(coordinate.longitude, coordinate.latitude)
    var locationUpdateCount by remember { mutableIntStateOf(0) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(currentPoint)
            zoom(15.0)
        }
    }

    // --- LÓGICA DE CÁMARA ---

    // 1. Centrado automático inicial (GPS)
    LaunchedEffect(coordinate) {
        if (locationUpdateCount < 2) {
            mapViewportState.easeTo(
                cameraOptions { center(currentPoint); zoom(15.0) },
                MapAnimationOptions.mapAnimationOptions { duration(1000) }
            )
            locationUpdateCount++
        }
    }

    // 2. Acción del botón de centrar (Punto Verde)
    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0) {
            mapViewportState.easeTo(
                cameraOptions { center(currentPoint); zoom(15.0) },
                MapAnimationOptions.mapAnimationOptions { duration(800) }
            )
        }
    }

    // 3. Acción del botón Zoom In (+) -> AQUÍ ESTABA EL ERROR
    LaunchedEffect(zoomInTrigger) {
        if (zoomInTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions { zoom(currentZoom + 1.0) },
                MapAnimationOptions.mapAnimationOptions { duration(300) }
            )
        }
    }

    // 4. Acción del botón Zoom Out (-) -> AQUÍ ESTABA EL ERROR
    LaunchedEffect(zoomOutTrigger) {
        if (zoomOutTrigger > 0) {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
            mapViewportState.easeTo(
                cameraOptions { zoom(currentZoom - 1.0) },
                MapAnimationOptions.mapAnimationOptions { duration(300) }
            )
        }
    }

    // --- RENDERIZADO DEL MAPA ---
    MapboxMap(
        modifier = modifier,
        mapViewportState = mapViewportState,
        style = {
            MapStyle(style = Style.MAPBOX_STREETS)
        }
    ) {
        // A. Dibujamos la ruta (Calles reales)
        if (polylinePoints.isNotEmpty()) {
            PolylineAnnotation(
                points = polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
            ) {
                lineColor = Color(0xFF007AFF) // Azul pro
                lineWidth = 6.0
                lineOpacity = 0.8
            }
        }

        // B. Paradas (Puntos Rojos)
        stops.forEach { stop ->
            CircleAnnotation(point = Point.fromLngLat(stop.longitude, stop.latitude)) {
                circleRadius = 8.0
                circleColor = Color.Red
                circleStrokeWidth = 2.0
                circleStrokeColor = Color.White
            }
        }

        // C. Posición del Usuario (Punto Azul)
        CircleAnnotation(point = currentPoint) {
            circleRadius = 10.0
            circleColor = Color.Blue
            circleStrokeWidth = 3.0
            circleStrokeColor = Color.White
        }
    }
}