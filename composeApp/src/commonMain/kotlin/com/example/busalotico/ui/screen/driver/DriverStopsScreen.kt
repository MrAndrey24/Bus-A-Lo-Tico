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
package com.example.busalotico.ui.screen.driver

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.header.ScreenHeader

@Composable
fun DriverStopsScreen(modifier: Modifier = Modifier) {
  Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF4F4F6)) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Paradas",
          subtitle = "Vista del conductor",
      )

      Surface(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          shape = RoundedCornerShape(18.dp),
          color = Color.White,
          border = BorderStroke(1.dp, Color(0xFFE5E6EB)),
      ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
              text = "PARADAS",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF8B8E95),
              fontWeight = FontWeight.Medium,
          )
          Text(
              text = "Pendiente de asignación",
              style = MaterialTheme.typography.titleMedium,
              color = Color(0xFF1F2126),
              fontWeight = FontWeight.SemiBold,
          )
          Text(
              text =
                  "Cuando exista una ruta asignada al conductor, aquí podremos mostrar la lista de paradas en orden.",
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF6F737B),
          )
        }
      }
    }
  }
}
