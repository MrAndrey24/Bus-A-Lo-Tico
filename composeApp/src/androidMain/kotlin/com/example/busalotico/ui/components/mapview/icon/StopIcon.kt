/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StopIcon(order: Int) {
  Box(
      contentAlignment = Alignment.Center,
      modifier =
          Modifier.size(28.dp)
              .background(Color(0xFFDC2626), CircleShape)
              .border(2.dp, Color.White, CircleShape),
  ) {
    Text(
        text = order.toString(),
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
    )
  }
}
