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
package com.example.busalotico.ui.components.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SelectableFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    showChevron: Boolean = true,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
) {
  val shape = RoundedCornerShape(999.dp)
  val backgroundColor =
      if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
  val textColor =
      if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

  Surface(
      modifier = modifier.clip(shape).clickable(onClick = onClick),
      shape = shape,
      color = backgroundColor,
      border = border,
  ) {
    Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(text = text, style = MaterialTheme.typography.labelMedium, color = textColor)

      if (showChevron) {
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "▾",
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = if (selected) 0.9f else 0.75f),
        )
      }
    }
  }
}
