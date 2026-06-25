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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.RouteEditorTab
import com.example.busalotico.model.RouteFormState
import com.example.busalotico.model.ScheduleFormState
import com.example.busalotico.model.StopFormState
import com.example.busalotico.ui.components.desktop.DesktopListCard
import com.example.busalotico.ui.screens.desktop.DesktopTab
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
import com.example.sharedlogic.presentation.admin.AdminCompanyUiState
import kotlin.collections.get

@Composable
fun RouteManagementSection(
    uiState: AdminCompanyUiState,
    selectedTab: RouteEditorTab,
    onTabSelected: (RouteEditorTab) -> Unit,
    routeForm: RouteFormState,
    onRouteFormChange: (RouteFormState) -> Unit,
    scheduleForm: ScheduleFormState,
    onScheduleFormChange: (ScheduleFormState) -> Unit,
    stopForm: StopFormState,
    onStopFormChange: (StopFormState) -> Unit,
    isCreatingRoute: Boolean,
    onCreateRoute: () -> Unit,
    onSelectRoute: (String) -> Unit,
    onSaveRoute: () -> Unit,
    onSelectSchedule: (RouteSchedule) -> Unit,
    onSaveSchedule: () -> Unit,
    onDeleteSchedule: () -> Unit,
    onSelectStop: (RouteStop) -> Unit,
    onSaveStop: () -> Unit,
    onDeleteStop: () -> Unit,
    onMoveStop: (String, Int) -> Unit,
    onClearScheduleForm: () -> Unit,
    onClearStopForm: () -> Unit,
) {
  Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
    ElevatedCard(
        modifier = Modifier.width(380.dp).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      var search by rememberSaveable { mutableStateOf("") }
      val filteredRoutes =
          remember(uiState.routes, search) {
            uiState.routes.filter { route ->
              search.isBlank() ||
                  route.name.contains(search, ignoreCase = true) ||
                  route.code.contains(search, ignoreCase = true)
            }
          }
      Column(
          modifier = Modifier.fillMaxSize().padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text("Rutas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar ruta") },
            singleLine = true,
        )
        Button(
            onClick = onCreateRoute,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
          Icon(Icons.Default.Add, contentDescription = null)
          Spacer(Modifier.width(8.dp))
          Text("Nueva ruta")
        }
        Divider(color = Color(0xFFEAECEF))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 12.dp),
        ) {
          items(filteredRoutes, key = { it.id }) { route ->
            val selected = !isCreatingRoute && uiState.selectedRoute?.id == route.id
            DesktopListCard(
                title = route.name,
                subtitle =
                    "${route.startLocation.district.orEmpty()} → ${route.endLocation.district.orEmpty()}",
                icon = Icons.Default.LocationOn,
                selected = selected,
                onClick = { onSelectRoute(route.id) },
            )
          }
        }
      }
    }

    ElevatedCard(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          DesktopTab("Información", selectedTab == RouteEditorTab.DETAILS) {
            onTabSelected(RouteEditorTab.DETAILS)
          }
          DesktopTab(
              "Horarios",
              selectedTab == RouteEditorTab.SCHEDULES,
              enabled = !isCreatingRoute && uiState.selectedRoute != null,
          ) {
            onTabSelected(RouteEditorTab.SCHEDULES)
          }
          DesktopTab(
              "Paradas",
              selectedTab == RouteEditorTab.STOPS,
              enabled = !isCreatingRoute && uiState.selectedRoute != null,
          ) {
            onTabSelected(RouteEditorTab.STOPS)
          }
        }
        when (selectedTab) {
          RouteEditorTab.DETAILS ->
              RouteDetailsEditor(
                  routeForm = routeForm,
                  onRouteFormChange = onRouteFormChange,
                  companyLabel =
                      routeForm.companyId.ifBlank {
                        uiState.companyNames[uiState.selectedRoute?.companyId] ?: "Sin empresa"
                      },
                  onSaveRoute = onSaveRoute,
                  isSaving = uiState.isSavingRoute,
              )
          RouteEditorTab.SCHEDULES ->
              RouteSchedulesEditor(
                  route = uiState.selectedRoute,
                  drivers = uiState.companyDrivers,
                  scheduleForm = scheduleForm,
                  onScheduleFormChange = onScheduleFormChange,
                  onSelectSchedule = onSelectSchedule,
                  onSaveSchedule = onSaveSchedule,
                  onDeleteSchedule = onDeleteSchedule,
                  onClearForm = onClearScheduleForm,
                  isSaving = uiState.isSavingSchedule,
              )
          RouteEditorTab.STOPS ->
              RouteStopsEditor(
                  route = uiState.selectedRoute,
                  stopForm = stopForm,
                  onStopFormChange = onStopFormChange,
                  onSelectStop = onSelectStop,
                  onSaveStop = onSaveStop,
                  onDeleteStop = onDeleteStop,
                  onMoveStop = onMoveStop,
                  onClearForm = onClearStopForm,
                  isSaving = uiState.isSavingStop,
              )
        }
      }
    }
  }
}
