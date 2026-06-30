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
package com.example.busalotico.ui.screens.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.AdminSection
import com.example.busalotico.model.DriverFormState
import com.example.busalotico.model.DriverFormStateSaver
import com.example.busalotico.model.RouteEditorTab
import com.example.busalotico.model.RouteFormState
import com.example.busalotico.model.RouteFormStateSaver
import com.example.busalotico.model.ScheduleFormState
import com.example.busalotico.model.ScheduleFormStateSaver
import com.example.busalotico.model.StopFormState
import com.example.busalotico.model.StopFormStateSaver
import com.example.busalotico.model.toFormState
import com.example.busalotico.model.toRoute
import com.example.busalotico.model.toSchedule
import com.example.busalotico.model.toStop
import com.example.busalotico.ui.components.desktop.DesktopFeedbackCard
import com.example.busalotico.ui.components.desktop.DesktopHeader
import com.example.busalotico.ui.components.desktop.DesktopSidebar
import com.example.busalotico.ui.screens.admin.AdminOverviewSection
import com.example.busalotico.ui.screens.desktop.DriverManagementSection
import com.example.busalotico.ui.screens.route.RouteManagementSection
import com.example.sharedlogic.presentation.admin.AdminCompanyViewModel
import com.example.sharedlogic.presentation.company.CompanyListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompanyAdminDesktopApp(
    currentUserName: String,
    currentUserCompanyId: String?,
    onSignOut: () -> Unit,
) {
  val viewModel = koinViewModel<AdminCompanyViewModel>()
  val companyListViewModel = koinViewModel<CompanyListViewModel>()
  val uiState by viewModel.uiState.collectAsState()
  val companyListUiState by companyListViewModel.uiState.collectAsState()
  var selectedSection by rememberSaveable { mutableStateOf(AdminSection.DASHBOARD) }
  var selectedTab by rememberSaveable { mutableStateOf(RouteEditorTab.DETAILS) }
  var isCreatingRoute by rememberSaveable { mutableStateOf(false) }
  var routeForm by
      rememberSaveable(stateSaver = RouteFormStateSaver) { mutableStateOf(RouteFormState()) }
  var scheduleForm by
      rememberSaveable(stateSaver = ScheduleFormStateSaver) { mutableStateOf(ScheduleFormState()) }
  var stopForm by
      rememberSaveable(stateSaver = StopFormStateSaver) { mutableStateOf(StopFormState()) }
  var driverForm by
      rememberSaveable(stateSaver = DriverFormStateSaver) { mutableStateOf(DriverFormState()) }

  LaunchedEffect(uiState.selectedRoute?.id, isCreatingRoute) {
    if (!isCreatingRoute) {
      routeForm = uiState.selectedRoute?.toFormState() ?: RouteFormState()
      scheduleForm = ScheduleFormState()
      stopForm = StopFormState()
    }
  }

  LaunchedEffect(uiState.companyDrivers) {
    if (driverForm.id.isBlank() && uiState.companyDrivers.isNotEmpty()) {
      driverForm = uiState.companyDrivers.first().toFormState()
    }
  }

  LaunchedEffect(currentUserCompanyId) {
    if (!currentUserCompanyId.isNullOrBlank()) {
      viewModel.setCompanyScope(currentUserCompanyId)
      companyListViewModel.loadCompanies()
    }
  }

  val currentCompanyLabel =
      remember(currentUserCompanyId, companyListUiState.companies, uiState.companyNames) {
        currentUserCompanyId
            ?.let { companyId ->
              companyListUiState.companies.firstOrNull { it.id == companyId }?.name
                  ?: uiState.companyNames[companyId]
                  ?: companyId
            }
            .orEmpty()
      }

  Scaffold(
      containerColor = Color(0xFFF6F7FB),
  ) { paddingValues ->
    Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      DesktopSidebar(
          selectedSection = selectedSection,
          onSectionSelected = { selectedSection = it },
      )

      Column(
          modifier = Modifier.fillMaxSize().padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        DesktopHeader(
            title =
                when (selectedSection) {
                  AdminSection.DASHBOARD -> "Panel empresarial"
                  AdminSection.ROUTES -> "Gestión de rutas"
                  AdminSection.DRIVERS -> "Gestión de conductores"
                },
            subtitle =
                when (selectedSection) {
                  AdminSection.DASHBOARD -> "Resumen operativo rutas y conductores."
                  AdminSection.ROUTES -> "Administra la información de rutas y horarios y paradas."
                  AdminSection.DRIVERS -> "Mantenimiento de conductores vinculados a la empresa."
                },
            currentUserName = currentUserName,
            currentCompanyLabel = currentCompanyLabel,
            onRefresh = {
              viewModel.refresh()
              companyListViewModel.loadCompanies()
            },
            onSignOut = onSignOut,
        )

        uiState.successMessage?.let { message ->
          DesktopFeedbackCard(
              message = message,
              background = Color(0xFFEAF7EE),
              content = Color(0xFF1B6E3D),
              onDismiss = viewModel::dismissMessages,
          )
        }
        uiState.errorMessage?.let { message ->
          DesktopFeedbackCard(
              message = message,
              background = Color(0xFFFDECEC),
              content = Color(0xFFB3261E),
              onDismiss = viewModel::dismissMessages,
          )
        }

        when (selectedSection) {
          AdminSection.DASHBOARD ->
              AdminOverviewSection(
                  uiState = uiState,
                  onDeleteIncident = { tripExecutionId, incidentId ->
                    viewModel.deleteIncident(tripExecutionId, incidentId)
                  },
              )
          AdminSection.ROUTES ->
              RouteManagementSection(
                  uiState = uiState,
                  selectedTab = selectedTab,
                  onTabSelected = { selectedTab = it },
                  routeForm = routeForm,
                  onRouteFormChange = { routeForm = it },
                  scheduleForm = scheduleForm,
                  onScheduleFormChange = { scheduleForm = it },
                  stopForm = stopForm,
                  onStopFormChange = { stopForm = it },
                  isCreatingRoute = isCreatingRoute,
                  onCreateRoute = {
                    isCreatingRoute = true
                    selectedTab = RouteEditorTab.DETAILS
                    routeForm = RouteFormState(companyId = currentUserCompanyId.orEmpty())
                    scheduleForm = ScheduleFormState()
                    stopForm = StopFormState()
                  },
                  onSelectRoute = {
                    isCreatingRoute = false
                    viewModel.selectRoute(it)
                  },
                  onSaveRoute = {
                    viewModel.saveRoute(routeForm.toRoute())
                    isCreatingRoute = false
                  },
                  onSelectSchedule = { scheduleForm = it.toFormState() },
                  onSaveSchedule = {
                    val isEditingSchedule = scheduleForm.id.isNotBlank()
                    uiState.selectedRoute?.id?.let { routeId ->
                      viewModel.saveSchedule(routeId, scheduleForm.toSchedule())
                      if (!isEditingSchedule) {
                        scheduleForm = ScheduleFormState()
                      }
                    }
                  },
                  onDeleteSchedule = {
                    uiState.selectedRoute?.id?.let { routeId ->
                      viewModel.deleteSchedule(routeId, scheduleForm.id)
                      scheduleForm = ScheduleFormState()
                    }
                  },
                  onSelectStop = { stopForm = it.toFormState() },
                  onSaveStop = {
                    uiState.selectedRoute?.id?.let { routeId ->
                      viewModel.saveStop(routeId, stopForm.toStop())
                      stopForm = StopFormState()
                    }
                  },
                  onDeleteStop = {
                    uiState.selectedRoute?.id?.let { routeId ->
                      viewModel.deleteStop(routeId, stopForm.id)
                      stopForm = StopFormState()
                    }
                  },
                  onMoveStop = { stopId, direction ->
                    uiState.selectedRoute?.id?.let { routeId ->
                      viewModel.moveStop(routeId, stopId, direction)
                    }
                  },
                  onClearScheduleForm = { scheduleForm = ScheduleFormState() },
                  onClearStopForm = { stopForm = StopFormState() },
              )
          AdminSection.DRIVERS ->
              DriverManagementSection(
                  uiState = uiState,
                  driverForm = driverForm,
                  onDriverFormChange = { driverForm = it },
                  onSelectDriver = { driverForm = it.toFormState() },
                  onClearDriverForm = { driverForm = DriverFormState() },
                  onSaveDriver = {
                    val companyId = currentUserCompanyId ?: uiState.selectedRoute?.companyId
                    viewModel.saveDriver(
                        name = driverForm.name,
                        email = driverForm.email,
                        password = driverForm.password,
                        companyId = companyId,
                    )
                  },
                  onDeleteDriver = {
                    viewModel.deleteDriver(driverForm.id)
                    driverForm = DriverFormState()
                  },
              )
        }
      }
    }
  }
}
