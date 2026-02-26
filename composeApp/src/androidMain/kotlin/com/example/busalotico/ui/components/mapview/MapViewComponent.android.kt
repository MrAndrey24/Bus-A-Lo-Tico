package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shared.domain.entities.Coordinate
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
actual fun MapViewComponent(
    modifier: Modifier,
    coordinate: Coordinate
) {
    val currentPoint = Point.fromLngLat(coordinate.longitude, coordinate.latitude)


    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(currentPoint)
            zoom(15.0)
        }
    }

    LaunchedEffect(coordinate) {
        mapViewportState.easeTo(
            cameraOptions {
                center(currentPoint)
                zoom(15.0)
            },
            MapAnimationOptions.mapAnimationOptions {
                duration(1000)
            }
        )
    }

    MapboxMap(
        modifier = modifier,
        mapViewportState = mapViewportState,
        style = {
            MapStyle(style = Style.MAPBOX_STREETS)
        }
    ) {
        CircleAnnotation(point = currentPoint) {
            circleRadius = 10.0
            circleColor = Color.Red
            circleStrokeWidth = 3.0
            circleStrokeColor = Color.White
        }
    }
}