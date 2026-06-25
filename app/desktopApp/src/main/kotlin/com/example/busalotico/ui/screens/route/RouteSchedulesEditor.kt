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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.busalotico.model.ScheduleFormState
import com.example.busalotico.model.toDisplayMeridiemTime
import com.example.busalotico.ui.components.EmptyPanelState
import com.example.busalotico.ui.components.admin.AdminTextField
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import kotlin.collections.forEach

@Composable
fun RouteSchedulesEditor(
    route: Route?,
    drivers: List<Driver>,
    scheduleForm: ScheduleFormState,
    onScheduleFormChange: (ScheduleFormState) -> Unit,
    onSelectSchedule: (RouteSchedule) -> Unit,
    onSaveSchedule: () -> Unit,
    onDeleteSchedule: () -> Unit,
    onClearForm: () -> Unit,
    isSaving: Boolean,
) {
  if (route == null) {
    EmptyPanelState("Selecciona una ruta para editar horarios.")
    return
  }

  val selectedDriverName = drivers.firstOrNull { it.id == scheduleForm.driverId }?.name.orEmpty()
  var driverExpanded by remember(drivers, scheduleForm.id) { mutableStateOf(false) }

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
            "Horarios existentes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(route.schedules.sortedBy { it.departureTime }, key = { it.id }) { schedule ->
            val driverName =
                drivers.firstOrNull { it.id == schedule.driverId }?.name ?: schedule.driverId
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onSelectSchedule(schedule) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
              Row(
                  modifier = Modifier.fillMaxWidth().padding(16.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Column {
                  Text(
                      "${schedule.departureTime.toDisplayMeridiemTime()} → ${schedule.arrivalTime.toDisplayMeridiemTime()}",
                      fontWeight = FontWeight.SemiBold,
                  )
                  Text("Chofer: $driverName", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF718096))
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
        Text("Horario", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
              text = "Conductor",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Box {
            Surface(
                modifier =
                    Modifier.fillMaxWidth().clickable(enabled = drivers.isNotEmpty()) {
                      driverExpanded = !driverExpanded
                    },
                shape = RoundedCornerShape(16.dp),
                color = if (drivers.isEmpty()) Color(0xFFF9FAFC) else Color.White,
                border = BorderStroke(1.dp, Color(0xFFE3E8F0)),
            ) {
              Row(
                  modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                    text =
                        selectedDriverName.ifBlank {
                          if (drivers.isEmpty()) "No hay conductores disponibles"
                          else "Selecciona un conductor"
                        },
                    style = MaterialTheme.typography.bodyLarge,
                    color =
                        if (selectedDriverName.isBlank()) {
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
                expanded = driverExpanded && drivers.isNotEmpty(),
                onDismissRequest = { driverExpanded = false },
                modifier = Modifier.width(320.dp).background(Color.White),
            ) {
              drivers.forEach { driver ->
                DropdownMenuItem(
                    text = {
                      Text(
                          text = driver.name,
                          style = MaterialTheme.typography.bodyMedium,
                          fontWeight =
                              if (scheduleForm.driverId == driver.id) FontWeight.SemiBold
                              else FontWeight.Normal,
                      )
                    },
                    onClick = {
                      onScheduleFormChange(scheduleForm.copy(driverId = driver.id))
                      driverExpanded = false
                    },
                )
              }
            }
          }
        }
        AdminTextField(
            "Hora salida",
            scheduleForm.departureTime,
            { onScheduleFormChange(scheduleForm.copy(departureTime = it)) },
            supportingText = "Formato 07:00 o 7:00 AM",
        )
        AdminTextField(
            "Hora llegada",
            scheduleForm.arrivalTime,
            { onScheduleFormChange(scheduleForm.copy(arrivalTime = it)) },
            supportingText = "Formato 08:25 o 8:25 AM",
        )
        if (drivers.isEmpty()) {
          Text(
              text = "No hay conductores registrados para esta empresa.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              style = MaterialTheme.typography.bodyMedium,
          )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onClearForm, enabled = !isSaving) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo horario")
            Spacer(Modifier.width(6.dp))
            Text("Nuevo")
          }
          Button(
              onClick = onSaveSchedule,
              enabled =
                  !isSaving &&
                      scheduleForm.driverId.isNotBlank() &&
                      scheduleForm.departureTime.isNotBlank() &&
                      scheduleForm.arrivalTime.isNotBlank(),
              shape = RoundedCornerShape(16.dp),
          ) {
            if (isSaving) {
              CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
              Icon(Icons.Default.Save, contentDescription = "Guardar horario")
            }
          }
          if (scheduleForm.id.isNotBlank()) {
            TextButton(onClick = onDeleteSchedule, enabled = !isSaving) {
              Icon(Icons.Default.Delete, contentDescription = "Eliminar horario")
            }
          }
        }
      }
    }
  }
}
