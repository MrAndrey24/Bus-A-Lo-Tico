/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
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
