package com.example.busalotico.ui.components.mapview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.domain.entities.Coordinate

@Composable
expect fun MapViewComponent(
    modifier: Modifier = Modifier,
    coordinate: Coordinate
)