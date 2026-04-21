/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo

@Composable
fun FitCameraToRoute(
    recenterTrigger: Int,
    polylinePoints: List<Coordinate>,
    stops: List<RouteStop>,
) {
  var lastFittedPointsHash by remember { mutableIntStateOf(0) }
  var lastFittedRecenterTrigger by remember { mutableIntStateOf(0) }

  MapEffect(polylinePoints, stops, recenterTrigger) { mapView ->
    val currentPointsHash = polylinePoints.hashCode() + stops.hashCode()

    val pointsChanged = currentPointsHash != lastFittedPointsHash
    val recenterRequested = recenterTrigger > lastFittedRecenterTrigger

    if (
        (pointsChanged || recenterRequested) && (polylinePoints.isNotEmpty() || stops.isNotEmpty())
    ) {
      val coordinatesToFrame =
          if (polylinePoints.isNotEmpty()) {
            polylinePoints.map { Point.fromLngLat(it.longitude, it.latitude) }
          } else {
            stops.map { Point.fromLngLat(it.longitude, it.latitude) }
          }

      val cameraOptions =
          mapView.mapboxMap.cameraForCoordinates(
              coordinatesToFrame,
              EdgeInsets(100.0, 100.0, 100.0, 100.0),
          )

      mapView.mapboxMap.easeTo(
          cameraOptions,
          MapAnimationOptions.mapAnimationOptions { duration(800) },
      )

      lastFittedPointsHash = currentPointsHash
      lastFittedRecenterTrigger = recenterTrigger
    }
  }
}
