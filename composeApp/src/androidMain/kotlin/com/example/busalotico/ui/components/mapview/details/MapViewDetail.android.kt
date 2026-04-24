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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.busalotico.ui.components.mapview.route.RouteAnnotations
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
actual fun MapViewDetail(
    modifier: Modifier,
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop>,
    polylinePoints: List<Coordinate>,
    busLocation: Coordinate?,
) {
  val mapViewportState = rememberMapViewportState()

  // Zoom In (+)
  LaunchedEffect(zoomInTrigger) {
    if (zoomInTrigger > 0) {
      val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
      mapViewportState.easeTo(
          cameraOptions { zoom(currentZoom + 1.0) },
          MapAnimationOptions.mapAnimationOptions { duration(300) },
      )
    }
  }

  // Zoom Out (-)
  LaunchedEffect(zoomOutTrigger) {
    if (zoomOutTrigger > 0) {
      val currentZoom = mapViewportState.cameraState?.zoom ?: 15.0
      mapViewportState.easeTo(
          cameraOptions { zoom(currentZoom - 1.0) },
          MapAnimationOptions.mapAnimationOptions { duration(300) },
      )
    }
  }

  MapboxMap(
      modifier = modifier,
      mapViewportState = mapViewportState,
      style = { MapStyle(style = Style.MAPBOX_STREETS) },
  ) {
    FitCameraToRoute(recenterTrigger, polylinePoints, stops)

    RouteAnnotations(polylinePoints = polylinePoints, stops = stops, busLocation = busLocation)
  }
}
