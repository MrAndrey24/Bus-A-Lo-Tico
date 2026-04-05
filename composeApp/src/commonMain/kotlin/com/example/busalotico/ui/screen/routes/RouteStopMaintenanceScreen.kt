package com.example.busalotico.ui.screen.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.route.RouteStopMaintenanceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RouteStopMaintenanceScreen(
    routeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: RouteStopMaintenanceViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(routeId) {
    viewModel.loadRouteStops(routeId)
  }

  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Mantenimiento de paradas",
          subtitle = "Detalle",
          leadingContent = {
            IconButton(onClick = onBackClick) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Volver",
                  tint = MaterialTheme.colorScheme.onSurface,
              )
            }
          },
      )

      LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        item {
          StopFormCard(
              name = uiState.name,
              latitude = uiState.latitude,
              longitude = uiState.longitude,
              order = uiState.order,
              selectedStopId = uiState.selectedStopId,
              isSaving = uiState.isSaving,
              isDeleting = uiState.isDeleting,
              onNameChanged = viewModel::onNameChanged,
              onLatitudeChanged = viewModel::onLatitudeChanged,
              onLongitudeChanged = viewModel::onLongitudeChanged,
              onOrderChanged = viewModel::onOrderChanged,
              onSave = viewModel::saveStop,
              onDelete = viewModel::deleteSelectedStop,
              onClear = viewModel::clearForm,
          )
        }

        uiState.errorMessage?.let { message ->
          item {
            StopFeedbackCard(
                message = message,
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                contentColor = MaterialTheme.colorScheme.error,
            )
          }
        }

        uiState.successMessage?.let { message ->
          item {
            StopFeedbackCard(
                message = message,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                contentColor = MaterialTheme.colorScheme.tertiary,
            )
          }
        }

        item {
          Text(
              text = "PARADAS DEFINIDAS",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
          )
        }

        if (uiState.isLoading) {
          item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
              CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
          }
        }

        if (!uiState.isLoading && uiState.stops.isEmpty()) {
          item {
            StopEmptyState()
          }
        }

        items(uiState.stops, key = { it.id }) { stop ->
          StopListItem(
              stop = stop,
              isSelected = stop.id == uiState.selectedStopId,
              canMoveUp = uiState.stops.indexOfFirst { it.id == stop.id } > 0,
              canMoveDown = uiState.stops.indexOfFirst { it.id == stop.id } < uiState.stops.lastIndex,
              onClick = { viewModel.selectStop(stop) },
              onMoveUp = { viewModel.moveStopUp(stop.id) },
              onMoveDown = { viewModel.moveStopDown(stop.id) },
          )
        }
      }
    }
  }
}

@Composable
private fun StopFormCard(
    name: String,
    latitude: String,
    longitude: String,
    order: String,
    selectedStopId: String?,
    isSaving: Boolean,
    isDeleting: Boolean,
    onNameChanged: (String) -> Unit,
    onLatitudeChanged: (String) -> Unit,
    onLongitudeChanged: (String) -> Unit,
    onOrderChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
) {
  Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
          text = if (selectedStopId == null) "Nueva parada" else "Editar parada",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
      )

      OutlinedTextField(
          value = name,
          onValueChange = onNameChanged,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Nombre") },
          singleLine = true,
      )
      OutlinedTextField(
          value = latitude,
          onValueChange = onLatitudeChanged,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Latitud") },
          singleLine = true,
      )
      OutlinedTextField(
          value = longitude,
          onValueChange = onLongitudeChanged,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Longitud") },
          singleLine = true,
      )
      OutlinedTextField(
          value = order,
          onValueChange = onOrderChanged,
          modifier = Modifier.fillMaxWidth(),
          label = { Text("Orden") },
          singleLine = true,
      )

      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = !isSaving && !isDeleting,
        ) {
          Text(if (isSaving) "Guardando..." else "Guardar")
        }

        Button(
            onClick = onDelete,
            modifier = Modifier.weight(1f),
            enabled = selectedStopId != null && !isSaving && !isDeleting,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
        ) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text(if (isDeleting) "Eliminando..." else "Eliminar")
        }
      }

      if (selectedStopId != null) {
        Button(
            onClick = onClear,
            modifier = Modifier.fillMaxWidth(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        ) {
          Text("Limpiar selección")
        }
      }
    }
  }
}

@Composable
private fun StopFeedbackCard(
    message: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = containerColor,
      border = BorderStroke(1.dp, contentColor.copy(alpha = 0.12f)),
  ) {
    Text(
        text = message,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
    )
  }
}

@Composable
private fun StopListItem(
    stop: RouteStop,
    isSelected: Boolean,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
  Card(
      modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
      shape = RoundedCornerShape(18.dp),
      colors =
          CardDefaults.cardColors(
              containerColor =
                  if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.22f)
                  else MaterialTheme.colorScheme.surface,
          ),
      border =
          BorderStroke(
              width = 1.dp,
              color =
                  if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                  else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
          ),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
      ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            modifier = Modifier.padding(10.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
        )
      }
      Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stop.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "Lat: ${stop.latitude} · Lng: ${stop.longitude}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Orden: ${stop.order}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )
      }
      Column(
          verticalArrangement = Arrangement.spacedBy(2.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        IconButton(
            onClick = onMoveUp,
            enabled = canMoveUp,
        ) {
          Icon(
              imageVector = Icons.Default.KeyboardArrowUp,
              contentDescription = "Subir parada",
              tint =
                  if (canMoveUp) MaterialTheme.colorScheme.onSurfaceVariant
                  else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
          )
        }
        IconButton(
            onClick = onMoveDown,
            enabled = canMoveDown,
        ) {
          Icon(
              imageVector = Icons.Default.KeyboardArrowDown,
              contentDescription = "Bajar parada",
              tint =
                  if (canMoveDown) MaterialTheme.colorScheme.onSurfaceVariant
                  else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
          )
        }
      }
    }
  }
}

@Composable
private fun StopEmptyState() {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(18.dp),
      color = MaterialTheme.colorScheme.surface,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)),
  ) {
    Text(
        text = "Todavía no hay paradas registradas para esta ruta.",
        modifier = Modifier.padding(18.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
