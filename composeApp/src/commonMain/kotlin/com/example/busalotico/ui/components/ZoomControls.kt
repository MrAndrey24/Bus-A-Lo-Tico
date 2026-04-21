/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ZoomControls(onZoomIn: () -> Unit, onZoomOut: () -> Unit, modifier: Modifier = Modifier) {
  Card(
      modifier = modifier.width(48.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      IconButton(onClick = onZoomIn) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Acercar",
            tint = MaterialTheme.colorScheme.onSurface,
        )
      }

      HorizontalDivider(
          modifier = Modifier.padding(horizontal = 8.dp),
          thickness = 1.dp,
          color = MaterialTheme.colorScheme.outline,
      )

      IconButton(onClick = onZoomOut) {
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = "Alejar",
            tint = MaterialTheme.colorScheme.onSurface,
        )
      }
    }
  }
}
