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
package com.example.busalotico.ui.screens.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.StopFormState
import com.example.busalotico.ui.components.EmptyPanelState
import com.example.busalotico.ui.components.admin.AdminTextField
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteStop

@Composable
fun RouteStopsEditor(
    route: Route?,
    stopForm: StopFormState,
    onStopFormChange: (StopFormState) -> Unit,
    onSelectStop: (RouteStop) -> Unit,
    onSaveStop: () -> Unit,
    onDeleteStop: () -> Unit,
    onMoveStop: (String, Int) -> Unit,
    onClearForm: () -> Unit,
    isSaving: Boolean,
) {
  if (route == null) {
    EmptyPanelState("Selecciona una ruta para editar paradas.")
    return
  }

  Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
    ElevatedCard(
        modifier = Modifier.weight(0.58f).fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF9FAFC)),
    ) {
      Column(
          modifier = Modifier.fillMaxSize().padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(
            "Paradas definidas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(route.stops.sortedBy { it.order }, key = { it.id }) { stop ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
              Row(
                  modifier = Modifier.fillMaxWidth().padding(16.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Column(modifier = Modifier.weight(1f).clickable { onSelectStop(stop) }) {
                  Text("#${stop.order} · ${stop.name}", fontWeight = FontWeight.SemiBold)
                  Text(
                      "${stop.latitude}, ${stop.longitude}",
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(onClick = { onMoveStop(stop.id, -1) }) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Subir")
                  }
                  IconButton(onClick = { onMoveStop(stop.id, 1) }) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Bajar")
                  }
                }
              }
            }
          }
        }
      }
    }
    ElevatedCard(
        modifier = Modifier.weight(0.42f).fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxSize().padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Text("Parada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        AdminTextField("Nombre", stopForm.name, { onStopFormChange(stopForm.copy(name = it)) })
        AdminTextField(
            "Latitud",
            stopForm.latitude,
            { onStopFormChange(stopForm.copy(latitude = it)) },
        )
        AdminTextField(
            "Longitud",
            stopForm.longitude,
            { onStopFormChange(stopForm.copy(longitude = it)) },
        )
        AdminTextField("Orden", stopForm.order, { onStopFormChange(stopForm.copy(order = it)) })
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onClearForm, enabled = !isSaving) {
            Icon(Icons.Default.Add, contentDescription = "Nueva parada")
            Spacer(Modifier.width(6.dp))
            Text("Nuevo")
          }
          Button(
              onClick = onSaveStop,
              enabled =
                  !isSaving &&
                      stopForm.name.isNotBlank() &&
                      stopForm.latitude.isNotBlank() &&
                      stopForm.longitude.isNotBlank() &&
                      stopForm.order.isNotBlank(),
              shape = RoundedCornerShape(16.dp),
          ) {
            if (isSaving) {
              CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
              Icon(Icons.Default.Save, contentDescription = "Guardar parada")
            }
          }
          if (stopForm.id.isNotBlank()) {
            TextButton(onClick = onDeleteStop, enabled = !isSaving) {
              Icon(Icons.Default.Delete, contentDescription = "Eliminar parada")
            }
          }
        }
      }
    }
  }
}
