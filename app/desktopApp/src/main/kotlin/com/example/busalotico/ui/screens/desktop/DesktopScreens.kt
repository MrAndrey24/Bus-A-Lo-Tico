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
package com.example.busalotico.ui.screens.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DesktopFirebaseErrorApp(error: Throwable) {
  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    ElevatedCard(
        modifier = Modifier.width(720.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(28.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Text(
            text = "No se pudo inicializar Firebase en desktop",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                "La app de escritorio necesita cargar su configuración antes de consultar rutas, conductores y horarios.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFFDECEC),
        ) {
          Text(
              text = error.message ?: error.toString(),
              modifier = Modifier.padding(16.dp),
              color = Color(0xFFB3261E),
          )
        }
        Text(
            text =
                "Revisa el archivo desktopApp/src/jvmMain/resources/firebase.properties y vuelve a ejecutar la app.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
fun OverviewCard(title: String, value: String, subtitle: String) {
  ElevatedCard(
      modifier = Modifier.width(260.dp).height(160.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
          title,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
      Text(
          subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
fun DesktopTab(
    label: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
  Surface(
      modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
      shape = RoundedCornerShape(14.dp),
      color =
          when {
            !enabled -> Color(0xFFF3F4F7)
            selected -> Color(0xFF0F2746)
            else -> Color(0xFFF4F6FA)
          },
  ) {
    Text(
        text = label,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
        color = if (selected) Color.White else Color(0xFF4D5B70),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
    )
  }
}
