/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.busalotico.ui.components.mapview.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.busalotico.ui.components.mapview.route.RouteAnnotations
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.RouteStop
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
