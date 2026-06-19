/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.busalotico.ui.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownSelectField(
    value: String,
    placeholder: String,
    enabled: Boolean,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
  var expanded by remember { mutableStateOf(false) }
  var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
  val density = LocalDensity.current

  Box(modifier = modifier.fillMaxWidth()) {
    Surface(
        modifier =
            Modifier.fillMaxWidth()
                .height(50.dp)
                .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size }
                .clickable(enabled = enabled) { expanded = true },
        shape = RoundedCornerShape(16.dp),
        color =
            if (enabled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
    ) {
      Row(
          modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
            text = value.ifBlank { placeholder },
            color =
                if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    DropdownMenu(
        modifier = Modifier.width(with(density) { textFieldSize.width.toDp() }),
        expanded = expanded,
        onDismissRequest = { expanded = false },
        shape = RoundedCornerShape(14.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
    ) {
      options.forEach { option ->
        DropdownMenuItem(
            text = {
              Text(
                  text = option,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface,
              )
            },
            colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSurface),
            onClick = {
              onSelect(option)
              expanded = false
            },
        )
      }
    }
  }
}
