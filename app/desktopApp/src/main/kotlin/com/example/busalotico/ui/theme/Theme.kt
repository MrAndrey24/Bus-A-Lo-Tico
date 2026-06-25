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
package com.example.busalotico.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun DesktopAdminTheme(content: @Composable () -> Unit) {
  MaterialTheme(
      colorScheme =
          lightColorScheme(
              primary = Color(0xFF0F2746),
              secondary = Color(0xFF5D7691),
              background = Color(0xFFF6F7FB),
              surface = Color.White,
              surfaceVariant = Color(0xFFF4F6FA),
              outline = Color(0xFFE3E8F0),
          ),
      typography =
          Typography().run {
            copy(
                displaySmall = displaySmall.copy(fontSize = 30.sp),
                headlineMedium = headlineMedium.copy(fontSize = 24.sp),
                headlineSmall = headlineSmall.copy(fontSize = 20.sp),
                titleLarge = titleLarge.copy(fontSize = 18.sp),
                titleMedium = titleMedium.copy(fontSize = 16.sp),
                titleSmall = titleSmall.copy(fontSize = 14.sp),
                bodyLarge = bodyLarge.copy(fontSize = 14.sp),
                bodyMedium = bodyMedium.copy(fontSize = 13.sp),
                bodySmall = bodySmall.copy(fontSize = 12.sp),
                labelLarge = labelLarge.copy(fontSize = 13.sp),
                labelMedium = labelMedium.copy(fontSize = 12.sp),
                labelSmall = labelSmall.copy(fontSize = 11.sp),
            )
          },
      content = content,
  )
}
