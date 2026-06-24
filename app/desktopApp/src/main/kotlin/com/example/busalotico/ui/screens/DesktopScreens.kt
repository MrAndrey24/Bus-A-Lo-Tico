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
package com.example.busalotico.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.busalotico.model.toDisplayMeridiemTime
import com.example.busalotico.model.toFormState
import com.example.busalotico.model.toRoute
import com.example.busalotico.model.toSchedule
import com.example.busalotico.model.toStop
import com.example.busalotico.ui.components.AdminDropdownField
import com.example.busalotico.ui.components.AdminTextField
import com.example.busalotico.ui.components.DesktopFeedbackCard
import com.example.busalotico.ui.components.DesktopHeader
import com.example.busalotico.ui.components.DesktopListCard
import com.example.busalotico.ui.components.DesktopSidebar
import com.example.busalotico.ui.components.EmptyPanelState
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
import com.example.sharedlogic.domain.entities.UserRole
import com.example.sharedlogic.domain.entities.enums.TripIncidentType
import com.example.sharedlogic.domain.entities.geo.costaRicaGeoData
import com.example.sharedlogic.presentation.admin.AdminCompanyViewModel
import com.example.sharedlogic.presentation.company.CompanyListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopFirebaseErrorApp(error: Throwable) {
  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    ElevatedCard(
        modifier = Modifier.width(720.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(28.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Text(
            text = "No se pudo inicializar Firebase en desktop",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                "La app de escritorio necesita cargar su configuración antes de consultar rutas, conductores y horarios.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFFFDECEC),
        ) {
          Text(
              text = error.message ?: error.toString(),
              modifier = Modifier.padding(16.dp),
              color = Color(0xFFB3261E),
          )
        }
        Text(
            text =
                "Revisa el archivo desktopApp/src/jvmMain/resources/firebase.properties y vuelve a ejecutar la app.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
fun DesktopLoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onSignIn: (String, String) -> Unit,
) {
  var email by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF1F3F6)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    Column(
        modifier = Modifier.width(520.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      ElevatedCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(30.dp),
          colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
          elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
      ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
          Surface(
              modifier = Modifier.width(74.dp).height(74.dp),
              shape = RoundedCornerShape(22.dp),
              color = Color(0xFF122B4A),
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                  imageVector = Icons.Default.DirectionsBus,
                  contentDescription = null,
                  tint = Color.White,
              )
            }
          }
          Text(
              text = "Bus A Lo Tico",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
          )
          Text(
              text = "Viaja informado",
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.bodyLarge,
          )
          Text(
              text = "Ingresa con una cuenta administrativa para acceder al panel empresarial.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              style = MaterialTheme.typography.bodyMedium,
          )
          TextField(
              value = email,
              onValueChange = { email = it },
              modifier = Modifier.fillMaxWidth(),
              label = { Text("Correo") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              colors =
                  TextFieldDefaults.colors(
                      focusedContainerColor = Color(0xFFF1F3F6),
                      unfocusedContainerColor = Color(0xFFF1F3F6),
                      disabledContainerColor = Color(0xFFF1F3F6),
                      focusedIndicatorColor = Color.Transparent,
                      unfocusedIndicatorColor = Color.Transparent,
                      disabledIndicatorColor = Color.Transparent,
                  ),
          )
          TextField(
              value = password,
              onValueChange = { password = it },
              modifier = Modifier.fillMaxWidth(),
              label = { Text("Contraseña") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              visualTransformation =
                  if (passwordVisible) VisualTransformation.None
                  else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                  Icon(
                      imageVector =
                          if (passwordVisible) Icons.Outlined.Visibility
                          else Icons.Outlined.VisibilityOff,
                      contentDescription =
                          if (passwordVisible) "Contraseña visible" else "Contraseña oculta",
                  )
                }
              },
              colors =
                  TextFieldDefaults.colors(
                      focusedContainerColor = Color(0xFFF1F3F6),
                      unfocusedContainerColor = Color(0xFFF1F3F6),
                      disabledContainerColor = Color(0xFFF1F3F6),
                      focusedIndicatorColor = Color.Transparent,
                      unfocusedIndicatorColor = Color.Transparent,
                      disabledIndicatorColor = Color.Transparent,
                  ),
          )
          errorMessage?.let { message ->
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFDECEC)) {
              Text(
                  text = message,
                  modifier = Modifier.padding(14.dp),
                  color = Color(0xFFB3261E),
              )
            }
          }
          Button(
              onClick = { onSignIn(email.trim(), password) },
              enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
              modifier = Modifier.fillMaxWidth().height(54.dp),
              shape = RoundedCornerShape(16.dp),
          ) {
            if (isLoading) {
              CircularProgressIndicator(
                  modifier = Modifier.width(20.dp).height(20.dp),
                  color = Color.White,
                  strokeWidth = 2.dp,
              )
            } else {
              Text("Entrar al panel")
            }
          }
        }
      }
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
              modifier =
                  Modifier.width(18.dp)
                      .height(2.dp)
                      .background(Color(0xFF2C63D6), RoundedCornerShape(999.dp))
          )
          Box(
              modifier =
                  Modifier.width(30.dp)
                      .height(2.dp)
                      .background(Color(0xFFE25555), RoundedCornerShape(999.dp))
          )
          Box(
              modifier =
                  Modifier.width(18.dp)
                      .height(2.dp)
                      .background(Color(0xFF2C63D6), RoundedCornerShape(999.dp))
          )
        }
        Text(
            text = "PURA VIDA • COSTA RICA",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Medium,
        )
      }
    }
  }
}

@Composable
fun DesktopUnauthorizedScreen(
    userName: String,
    role: UserRole?,
    onSignOut: () -> Unit,
) {
  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    ElevatedCard(
        modifier = Modifier.width(560.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(32.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
            text = "Acceso restringido",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                buildString {
                  append(if (userName.isBlank()) "La sesión actual" else "$userName")
                  append(" no tiene permisos administrativos para usar esta aplicación.")
                },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF4E5)) {
          Text(
              text = "Rol detectado: ${role?.name ?: "DESCONOCIDO"}",
              modifier = Modifier.padding(14.dp),
              color = Color(0xFF8A5A00),
          )
        }
        TextButton(onClick = onSignOut) { Text("Cerrar sesión") }
      }
    }
  }
}

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
                    viewModel.saveDriver(driverForm.id, driverForm.name, companyId)
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

@Composable
fun AdminOverviewSection(
    uiState: com.example.sharedlogic.presentation.admin.AdminCompanyUiState,
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

@Composable
fun OverviewCard(title: String, value: String, subtitle: String) {
  ElevatedCard(
      modifier = Modifier.width(260.dp).height(160.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
          title,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
      Text(
          subtitle,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
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

@Composable
fun RouteManagementSection(
    uiState: com.example.sharedlogic.presentation.admin.AdminCompanyUiState,
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

@Composable
fun DesktopTab(
    label: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
  Surface(
      modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
      shape = RoundedCornerShape(14.dp),
      color =
          when {
            !enabled -> Color(0xFFF3F4F7)
            selected -> Color(0xFF0F2746)
            else -> Color(0xFFF4F6FA)
          },
  ) {
    Text(
        text = label,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
        color = if (selected) Color.White else Color(0xFF4D5B70),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
    )
  }
}

@Composable
fun RouteDetailsEditor(
    routeForm: RouteFormState,
    onRouteFormChange: (RouteFormState) -> Unit,
    companyLabel: String,
    onSaveRoute: () -> Unit,
    isSaving: Boolean,
) {
  val provinces = remember {
    costaRicaGeoData()
        .sortedBy { it.name.lowercase() }
        .map { province ->
          province.copy(
              cantons =
                  province.cantons
                      .sortedBy { it.name.lowercase() }
                      .map { canton ->
                        canton.copy(districts = canton.districts.sortedBy { it.lowercase() })
                      },
          )
        }
  }
  val startProvince = provinces.firstOrNull { it.name == routeForm.startProvince }
  val startCantons = startProvince?.cantons ?: emptyList()
  val startCanton = startCantons.firstOrNull { it.name == routeForm.startCanton }
  val startDistricts = startCanton?.districts ?: emptyList()

  val endProvince = provinces.firstOrNull { it.name == routeForm.endProvince }
  val endCantons = endProvince?.cantons ?: emptyList()
  val endCanton = endCantons.firstOrNull { it.name == routeForm.endCanton }
  val endDistricts = endCanton?.districts ?: emptyList()

  val scrollState = rememberScrollState()
  Column(
      modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(18.dp),
  ) {
    Text(
        "Datos de la ruta",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Código",
          routeForm.code,
          { onRouteFormChange(routeForm.copy(code = it)) },
          modifier = Modifier.weight(0.32f),
      )
      AdminTextField(
          "Nombre",
          routeForm.name,
          { onRouteFormChange(routeForm.copy(name = it)) },
          modifier = Modifier.weight(0.68f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Operador",
          routeForm.operator,
          { onRouteFormChange(routeForm.copy(operator = it)) },
          modifier = Modifier.weight(0.55f),
      )
      AdminTextField(
          "Empresa (ID)",
          routeForm.companyId,
          { onRouteFormChange(routeForm.copy(companyId = it)) },
          supportingText = companyLabel,
          modifier = Modifier.weight(0.45f),
      )
    }

    Text(
        "Punto de inicio",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminDropdownField(
          label = "Provincia inicio",
          value = routeForm.startProvince,
          placeholder = "Selecciona una provincia",
          options = provinces.map { it.name },
          onSelect = { provinceName ->
            onRouteFormChange(
                routeForm.copy(
                    startProvince = provinceName,
                    startCanton = "",
                    startDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Cantón inicio",
          value = routeForm.startCanton,
          placeholder = "Selecciona un cantón",
          options = startCantons.map { it.name },
          enabled = startCantons.isNotEmpty(),
          onSelect = { cantonName ->
            onRouteFormChange(
                routeForm.copy(
                    startCanton = cantonName,
                    startDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Distrito inicio",
          value = routeForm.startDistrict,
          placeholder = "Selecciona un distrito",
          options = startDistricts,
          enabled = startDistricts.isNotEmpty(),
          onSelect = { districtName ->
            onRouteFormChange(routeForm.copy(startDistrict = districtName))
          },
          modifier = Modifier.weight(1f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Tarifa CRC",
          routeForm.fareCrc,
          { onRouteFormChange(routeForm.copy(fareCrc = it)) },
          modifier = Modifier.weight(0.34f),
      )
      AdminTextField(
          "Latitud inicio",
          routeForm.startLatitude,
          { onRouteFormChange(routeForm.copy(startLatitude = it)) },
          modifier = Modifier.weight(0.33f),
      )
      AdminTextField(
          "Longitud inicio",
          routeForm.startLongitude,
          { onRouteFormChange(routeForm.copy(startLongitude = it)) },
          modifier = Modifier.weight(0.33f),
      )
    }

    Text(
        "Punto final",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminDropdownField(
          label = "Provincia final",
          value = routeForm.endProvince,
          placeholder = "Selecciona una provincia",
          options = provinces.map { it.name },
          onSelect = { provinceName ->
            onRouteFormChange(
                routeForm.copy(
                    endProvince = provinceName,
                    endCanton = "",
                    endDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Cantón final",
          value = routeForm.endCanton,
          placeholder = "Selecciona un cantón",
          options = endCantons.map { it.name },
          enabled = endCantons.isNotEmpty(),
          onSelect = { cantonName ->
            onRouteFormChange(
                routeForm.copy(
                    endCanton = cantonName,
                    endDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Distrito final",
          value = routeForm.endDistrict,
          placeholder = "Selecciona un distrito",
          options = endDistricts,
          enabled = endDistricts.isNotEmpty(),
          onSelect = { districtName ->
            onRouteFormChange(routeForm.copy(endDistrict = districtName))
          },
          modifier = Modifier.weight(1f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Latitud final",
          routeForm.endLatitude,
          { onRouteFormChange(routeForm.copy(endLatitude = it)) },
          modifier = Modifier.weight(1f),
      )
      AdminTextField(
          "Longitud final",
          routeForm.endLongitude,
          { onRouteFormChange(routeForm.copy(endLongitude = it)) },
          modifier = Modifier.weight(1f),
      )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = routeForm.acceptsCardPayment,
            onCheckedChange = { onRouteFormChange(routeForm.copy(acceptsCardPayment = it)) },
        )
        Text("Acepta tarjeta")
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = routeForm.isActive,
            onCheckedChange = { onRouteFormChange(routeForm.copy(isActive = it)) },
        )
        Text("Ruta activa")
      }
    }
    Button(
        onClick = onSaveRoute,
        enabled = !isSaving && routeForm.code.isNotBlank() && routeForm.name.isNotBlank(),
        shape = RoundedCornerShape(16.dp),
    ) {
      Icon(Icons.Default.Save, contentDescription = null)
      Spacer(Modifier.width(8.dp))
      Text(if (isSaving) "Guardando..." else "Guardar ruta")
    }
  }
}

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

@Composable
fun DriverManagementSection(
    uiState: com.example.sharedlogic.presentation.admin.AdminCompanyUiState,
    driverForm: DriverFormState,
    onDriverFormChange: (DriverFormState) -> Unit,
    onSelectDriver: (Driver) -> Unit,
    onClearDriverForm: () -> Unit,
    onSaveDriver: () -> Unit,
    onDeleteDriver: () -> Unit,
) {
  Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
    ElevatedCard(
        modifier = Modifier.width(360.dp).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxSize().padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Text(
            "Conductores",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                uiState.selectedRoute?.companyId?.let { companyId ->
                  uiState.companyNames[companyId] ?: companyId
                } ?: "Selecciona una ruta con empresa asignada para ver conductores.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Divider(color = Color(0xFFEAECEF))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(uiState.companyDrivers, key = { it.id }) { driver ->
            DesktopListCard(
                title = driver.name,
                subtitle = driver.id,
                icon = Icons.Default.Person,
                selected = driverForm.id == driver.id,
                onClick = { onSelectDriver(driver) },
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
          modifier = Modifier.fillMaxWidth().padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(18.dp),
      ) {
        Text(
            "Ficha de conductor",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        AdminTextField(
            "ID del conductor",
            driverForm.id,
            { onDriverFormChange(driverForm.copy(id = it)) },
        )
        AdminTextField(
            "Nombre",
            driverForm.name,
            { onDriverFormChange(driverForm.copy(name = it)) },
        )
        OutlinedTextField(
            value = uiState.selectedRoute?.companyId.orEmpty(),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Empresa vinculada") },
            supportingText = {
              Text(
                  uiState.selectedRoute?.companyId?.let { uiState.companyNames[it] ?: it }
                      ?: "Selecciona una ruta para asignar empresa."
              )
            },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onClearDriverForm, enabled = !uiState.isSavingDriver) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo conductor")
            Spacer(Modifier.width(6.dp))
            Text("Nuevo")
          }
          Button(
              onClick = onSaveDriver,
              enabled =
                  !uiState.isSavingDriver &&
                      driverForm.id.isNotBlank() &&
                      driverForm.name.isNotBlank() &&
                      uiState.selectedRoute?.companyId != null,
              shape = RoundedCornerShape(16.dp),
          ) {
            if (uiState.isSavingDriver) {
              CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
              Icon(Icons.Default.Save, contentDescription = "Guardar conductor")
            }
          }
          if (driverForm.id.isNotBlank()) {
            TextButton(onClick = onDeleteDriver, enabled = !uiState.isSavingDriver) {
              Icon(Icons.Default.Delete, contentDescription = "Eliminar conductor")
            }
          }
        }
      }
    }
  }
}
