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
package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun RolePlaceholderScreen(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
  Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF4F4F6)) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          text = title,
          style = MaterialTheme.typography.headlineSmall,
          color = Color(0xFF1F2126),
          fontWeight = FontWeight.SemiBold,
      )
      Text(
          text = subtitle,
          modifier = Modifier.padding(top = 8.dp),
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF6F737B),
      )
    }
  }
}
