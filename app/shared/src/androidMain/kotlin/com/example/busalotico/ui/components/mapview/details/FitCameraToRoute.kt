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
