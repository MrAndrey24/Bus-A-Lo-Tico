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
package com.example.busalotico.ui.components.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdminDropdownField(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    enabled: Boolean = true,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
  var expanded by remember(options, value) { mutableStateOf(false) }

  Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier.widthIn(min = 220.dp),
  ) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Box {
      Surface(
          modifier =
              Modifier.fillMaxWidth().clickable(enabled = enabled && options.isNotEmpty()) {
                expanded = !expanded
              },
          shape = RoundedCornerShape(16.dp),
          color = if (enabled) Color.White else Color(0xFFF9FAFC),
          border = BorderStroke(1.dp, Color(0xFFE3E8F0)),
      ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text =
                  value.ifBlank {
                    if (!enabled || options.isEmpty()) "Sin opciones disponibles" else placeholder
                  },
              style = MaterialTheme.typography.bodyLarge,
              color =
                  if (value.isBlank()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                  } else {
                    MaterialTheme.colorScheme.onSurface
                  },
          )
          Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
      DropdownMenu(
          expanded = expanded && enabled && options.isNotEmpty(),
          onDismissRequest = { expanded = false },
          modifier = Modifier.width(320.dp).background(Color.White),
      ) {
        options.forEach { option ->
          DropdownMenuItem(
              text = {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (value == option) FontWeight.SemiBold else FontWeight.Normal,
                )
              },
              onClick = {
                onSelect(option)
                expanded = false
              },
          )
        }
      }
    }
  }
}
