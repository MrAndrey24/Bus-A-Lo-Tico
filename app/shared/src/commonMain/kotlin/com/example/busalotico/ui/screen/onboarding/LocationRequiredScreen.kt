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
package com.example.busalotico.ui.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LocationRequiredScreen(onRetryClick: () -> Unit) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .padding(horizontal = 32.dp, vertical = 48.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(24.dp))

    Box(
        modifier =
            Modifier.size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(32.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
      Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = null,
          modifier = Modifier.size(64.dp),
          tint = MaterialTheme.colorScheme.error,
      )
    }

    Spacer(modifier = Modifier.height(40.dp))

    Text(
        text = "Acceso Requerido",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text =
            "Para que Bus-A-Lo-Tico funcione correctamente, necesitamos saber tu ubicación.\n\nPor favor, ve a los ajustes de tu dispositivo y permite el acceso a la ubicación.",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 24.sp,
    )

    Spacer(modifier = Modifier.weight(1f))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
      Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
            text = "¿Cómo activar?",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ajustes > Aplicaciones > Bus-A-Lo-Tico > Permisos > Ubicación",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onRetryClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
      Text("Abrir Ajustes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
  }
}
