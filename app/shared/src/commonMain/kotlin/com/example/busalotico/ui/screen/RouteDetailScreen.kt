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
package com.example.busalotico.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.RecenterButton
import com.example.busalotico.ui.components.ZoomControls
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.busalotico.ui.components.mapview.details.MapViewDetail
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.RouteStop
import com.example.sharedlogic.presentation.route.admin.RouteDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RouteDetailScreen(
    routeId: String,
    coordinate: Coordinate,
    viewModel: RouteDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    navigateToMapOnComplete: Boolean = false,
    onSimulationComplete: () -> Unit = {},
) {
  val polylinePoints by viewModel.polylinePoints.collectAsState()
  val stops by viewModel.stops.collectAsState()
  val busLocation by viewModel.busLocation.collectAsState()
  val routeName by viewModel.routeName.collectAsState()
  val currentStopIdx by viewModel.currentStopIndex.collectAsState()
  val stopEtas by viewModel.stopEtas.collectAsState()
  val isSimulationCompleted by viewModel.isSimulationCompleted.collectAsState()

  var recenterTrigger by remember { mutableIntStateOf(0) }
  var zoomInTrigger by remember { mutableIntStateOf(0) }
  var zoomOutTrigger by remember { mutableIntStateOf(0) }

  LaunchedEffect(coordinate) { if (coordinate.latitude != 0.0) recenterTrigger++ }

  Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    DetailedHeader(onBackClick = onBackClick)

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
      Spacer(Modifier.height(24.dp))

      Text(
          text = "RUTA ACTIVA",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
      )

      Spacer(Modifier.height(8.dp))

      RouteNameSection(routeName = routeName)

      Spacer(Modifier.height(16.dp))

      RouteStatusRow()

      Spacer(Modifier.height(24.dp))

      MapPreviewCard(
          recenterTrigger = recenterTrigger,
          zoomInTrigger = zoomInTrigger,
          zoomOutTrigger = zoomOutTrigger,
          stops = stops,
          polylinePoints = polylinePoints,
          busLocation = busLocation,
          onZoomIn = { zoomInTrigger++ },
          onZoomOut = { zoomOutTrigger++ },
          onRecenter = { recenterTrigger++ },
      )

      Spacer(Modifier.height(32.dp))

      Text(
          text = "Próximas paradas",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
      )

      Spacer(Modifier.height(16.dp))
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().weight(1f),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
    ) {
      itemsIndexed(stops) { index, stop ->
        TimelineStopRow(
            stop = stop,
            index = index,
            totalStops = stops.size,
            currentStopIdx = currentStopIdx,
            eta = stopEtas.getOrNull(index),
        )
      }
      item { Spacer(Modifier.height(24.dp)) }
    }
  }

  if (isSimulationCompleted) {
    RouteCompletedDialog(
        onDismiss = {
          if (navigateToMapOnComplete) {
            viewModel.clearSimulation()
            onSimulationComplete()
          } else {
            viewModel.clearSimulation()
            onBackClick()
          }
        }
    )
  }
}

@Composable
private fun RouteCompletedDialog(onDismiss: () -> Unit) {
  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(onClick = onDismiss) { Text("Entendido", fontWeight = FontWeight.Bold) }
      },
      icon = { Text("🎉", fontSize = 32.sp) },
      title = {
        Text(
            text = "¡Ruta completada!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
      },
      text = {
        Text(
            text = "El autobús ha llegado al destino final.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      },
      shape = RoundedCornerShape(24.dp),
  )
}

@Composable
private fun DetailedHeader(onBackClick: () -> Unit) {
  ScreenHeader(
      title = "Bus A Lo Tico",
      subtitle = "DETALLE DE RUTA",
      leadingContent = {
        IconButton(onClick = onBackClick) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Volver",
              tint = MaterialTheme.colorScheme.onSurface,
          )
        }
      },
      trailingContent = {
        IconButton(onClick = {}) {
          Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Más",
              tint = MaterialTheme.colorScheme.onSurface,
          )
        }
      },
  )
}

@Composable
private fun RouteNameSection(routeName: String) {
  val cities = routeName.split("-").map { it.trim() }
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    if (cities.size >= 2) {
      Text(
          text = cities[0],
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
      )
      Box(
          modifier =
              Modifier.padding(horizontal = 12.dp)
                  .width(20.dp)
                  .height(2.dp)
                  .background(MaterialTheme.colorScheme.outlineVariant)
      )
      Text(
          text = cities[1],
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
      )
    } else {
      Text(
          text = routeName.ifBlank { "Cargando..." },
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
      )
    }
  }
}

@Composable
private fun RouteStatusRow() {
  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(99.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
      Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Box(modifier = Modifier.size(8.dp).background(Color(0xFF4CAF50), CircleShape))
        Text(
            text = "En tránsito",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )
      }
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(99.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
      Text(
          text = "Actualizado hace 1 min",
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
      )
    }
  }
}

@Composable
private fun MapPreviewCard(
    recenterTrigger: Int,
    zoomInTrigger: Int,
    zoomOutTrigger: Int,
    stops: List<RouteStop>,
    polylinePoints: List<Coordinate>,
    busLocation: Coordinate?,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenter: () -> Unit,
) {
  Card(
      modifier = Modifier.fillMaxWidth().height(240.dp),
      shape = RoundedCornerShape(24.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      MapViewDetail(
          modifier = Modifier.fillMaxSize(),
          recenterTrigger = recenterTrigger,
          zoomInTrigger = zoomInTrigger,
          zoomOutTrigger = zoomOutTrigger,
          stops = stops,
          polylinePoints = polylinePoints,
          busLocation = busLocation,
      )

      Column(
          modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        ZoomControls(onZoomIn = onZoomIn, onZoomOut = onZoomOut)
        RecenterButton(onClick = onRecenter)
      }
    }
  }
}

@Composable
private fun TimelineStopRow(
    stop: RouteStop,
    index: Int,
    totalStops: Int,
    currentStopIdx: Int,
    eta: String? = null,
) {
  val isCompleted = index < currentStopIdx
  val isCurrent = index == currentStopIdx
  val isLast = index == totalStops - 1

  Row(
      modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
      verticalAlignment = Alignment.Top,
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
      Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
        when {
          isCompleted -> {
            Surface(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                    "✓",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
              }
            }
          }
          isCurrent -> {
            val infiniteTransition = rememberInfiniteTransition()
            val scale by
                infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.6f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart,
                        ),
                )
            val alpha by
                infiniteTransition.animateFloat(
                    initialValue = 0.4f,
                    targetValue = 0f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart,
                        ),
                )

            Box(contentAlignment = Alignment.Center) {
              Box(
                  modifier =
                      Modifier.size(24.dp)
                          .alpha(alpha)
                          .background(Color(0xFF4CAF50), CircleShape)
                          .padding(scale.dp * 10)
              )
              Surface(
                  modifier = Modifier.size(24.dp),
                  color = Color.White,
                  shape = CircleShape,
                  border = BorderStroke(2.dp, Color(0xFF4CAF50)),
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                      (index + 1).toString(),
                      color = Color(0xFF4CAF50),
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                  )
                }
              }
            }
          }
          else -> {
            Surface(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                shape = CircleShape,
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                    (index + 1).toString(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
              }
            }
          }
        }
      }
      if (!isLast) {
        Box(
            modifier =
                Modifier.width(2.dp).weight(1f).background(MaterialTheme.colorScheme.outlineVariant)
        )
      }
    }

    Spacer(Modifier.width(16.dp))

    Column(modifier = Modifier.weight(1f).padding(bottom = 24.dp)) {
      Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(
            text = stop.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color =
                if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f),
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = eta ?: "08:15",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color =
                if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
        )
      }

      Spacer(Modifier.height(4.dp))

      Text(
          text =
              when {
                isCompleted -> "Completada"
                isCurrent -> "Llegando ahora"
                isLast -> "Destino Final"
                else -> "En 12 mins aproximadamente"
              },
          style = MaterialTheme.typography.bodyMedium,
          color = if (isCurrent) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
      )
    }
  }
}
