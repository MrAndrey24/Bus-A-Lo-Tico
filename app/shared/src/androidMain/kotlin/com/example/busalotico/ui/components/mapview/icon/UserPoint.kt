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
package com.example.busalotico.ui.components.mapview.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation

@Composable
fun UserPoint(currentPoint: Point) {
  CircleAnnotation(point = currentPoint) {
    circleRadius = 10.0
    circleColor = Color.Blue
    circleStrokeWidth = 3.0
    circleStrokeColor = Color.White
  }
}
