/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecenterButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
  Card(
      modifier = modifier.size(48.dp),
      shape = CircleShape,
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
      Icon(
          imageVector = Icons.Default.MyLocation,
          contentDescription = "Centrar ubicación",
          tint = MaterialTheme.colorScheme.tertiary,
      )
    }
  }
}
