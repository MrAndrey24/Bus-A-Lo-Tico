/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.busalotico.ui.screens.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sharedlogic.domain.entities.UserRole

@Composable
fun DesktopUnauthorizedScreen(
    userName: String,
    role: UserRole?,
    onSignOut: () -> Unit,
) {
  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    ElevatedCard(
        modifier = Modifier.width(560.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
            text = "Acceso restringido",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                buildString {
                  append(if (userName.isBlank()) "La sesión actual" else userName)
                  append(" no tiene permisos administrativos para usar esta aplicación.")
                },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF4E5)) {
          Text(
              text = "Rol detectado: ${role?.name ?: "DESCONOCIDO"}",
              modifier = Modifier.padding(14.dp),
              color = Color(0xFF8A5A00),
          )
        }
        TextButton(onClick = onSignOut) { Text("Cerrar sesión") }
      }
    }
  }
}
