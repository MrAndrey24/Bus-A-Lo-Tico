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
package com.example.busalotico.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.screens.desktop.OverviewCard
import com.example.sharedlogic.domain.entities.enums.TripIncidentType
import com.example.sharedlogic.presentation.admin.AdminCompanyUiState

@Composable
fun AdminOverviewSection(
    uiState: AdminCompanyUiState,
    onDeleteIncident: (String, String) -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
    FlowRow(
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      OverviewCard("Rutas", uiState.routes.size.toString(), "Cubiertas por la empresa.")
      OverviewCard(
          "Conductores",
          uiState.companyDrivers.size.toString(),
          "Vinculados a la empresa seleccionada",
      )
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
            "Incidentes recientes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        if (uiState.recentIncidents.isEmpty()) {
          Text("No hay incidencias registradas para las rutas de esta empresa.")
        } else {
          LazyColumn(
              modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp),
          ) {
            items(uiState.recentIncidents, key = { "${it.tripExecutionId}:${it.incidentId}" }) {
                incident ->
              Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                  Surface(
                      modifier = Modifier.size(42.dp),
                      shape = CircleShape,
                      color = incidentChipBackground(incident.incidentType),
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(
                          imageVector = incidentIcon(incident.incidentType),
                          contentDescription = null,
                          tint = incidentChipContent(incident.incidentType),
                          modifier = Modifier.size(20.dp),
                      )
                    }
                  }
                  Column(
                      modifier = Modifier.weight(1f),
                      verticalArrangement = Arrangement.spacedBy(4.dp),
                  ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                      Text(incident.routeName, fontWeight = FontWeight.SemiBold)
                      Row(
                          horizontalArrangement = Arrangement.spacedBy(8.dp),
                          verticalAlignment = Alignment.CenterVertically,
                      ) {
                        AssistChip(
                            onClick = {},
                            enabled = false,
                            label = { Text(formatIncidentType(incident.incidentType)) },
                            leadingIcon = {
                              Icon(
                                  imageVector = incidentIcon(incident.incidentType),
                                  contentDescription = null,
                                  tint = incidentChipContent(incident.incidentType),
                                  modifier = Modifier.size(16.dp),
                              )
                            },
                            colors =
                                AssistChipDefaults.assistChipColors(
                                    disabledContainerColor =
                                        incidentChipBackground(incident.incidentType),
                                    disabledLabelColor = incidentChipContent(incident.incidentType),
                                ),
                        )
                        IconButton(
                            onClick = {
                              onDeleteIncident(incident.tripExecutionId, incident.incidentId)
                            },
                            modifier = Modifier.size(32.dp),
                        ) {
                          Icon(
                              imageVector = Icons.Default.Delete,
                              contentDescription = "Eliminar incidencia",
                              tint = MaterialTheme.colorScheme.onSurfaceVariant,
                              modifier = Modifier.size(16.dp),
                          )
                        }
                      }
                    }
                    Text(
                        text = "Chofer: ${incident.driverName}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = incident.incidentDetail,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                  }
                }
                HorizontalDivider(color = Color(0xFFEAECEF))
              }
            }
          }
        }
      }
    }
  }
}

private fun formatIncidentType(type: TripIncidentType): String =
    when (type) {
      TripIncidentType.ACCIDENT -> "ACCIDENTE"
      TripIncidentType.BREAKDOWN -> "AVERÍA"
      TripIncidentType.SECURITY -> "SEGURIDAD"
    }

private fun incidentChipBackground(type: TripIncidentType): Color =
    when (type) {
      TripIncidentType.ACCIDENT -> Color(0xFFFFE3E0)
      TripIncidentType.BREAKDOWN -> Color(0xFFFFF1D9)
      TripIncidentType.SECURITY -> Color(0xFFE4F1FF)
    }

private fun incidentChipContent(type: TripIncidentType): Color =
    when (type) {
      TripIncidentType.ACCIDENT -> Color(0xFFC0392B)
      TripIncidentType.BREAKDOWN -> Color(0xFFB9770E)
      TripIncidentType.SECURITY -> Color(0xFF2166B5)
    }

private fun incidentIcon(type: TripIncidentType): ImageVector =
    when (type) {
      TripIncidentType.ACCIDENT -> Icons.Default.ReportProblem
      TripIncidentType.BREAKDOWN -> Icons.Default.Build
      TripIncidentType.SECURITY -> Icons.Default.Security
    }
