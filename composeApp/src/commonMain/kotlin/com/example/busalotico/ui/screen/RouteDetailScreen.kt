package com.example.busalotico.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.RecenterButton
import com.example.busalotico.ui.components.ZoomControls
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.busalotico.ui.components.mapview.details.MapViewDetail
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.route.RouteDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------
@Composable
fun RouteDetailScreen(
    routeId: String,
    coordinate: Coordinate,
    viewModel: RouteDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
) {
  val polylinePoints by viewModel.polylinePoints.collectAsState()
  val stops by viewModel.stops.collectAsState()
  val busLocation by viewModel.busLocation.collectAsState()
  val routeName by viewModel.routeName.collectAsState()
  val currentStopIdx by viewModel.currentStopIndex.collectAsState()
  val stopEtas by viewModel.stopEtas.collectAsState()

  var recenterTrigger by remember { mutableIntStateOf(0) }
  var zoomInTrigger by remember { mutableIntStateOf(0) }
  var zoomOutTrigger by remember { mutableIntStateOf(0) }

  LaunchedEffect(coordinate) { if (coordinate.latitude != 0.0) recenterTrigger++ }

  Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    RouteHeader(routeName = routeName, onBackClick = onBackClick)

    RouteStatusChips()

    Spacer(Modifier.height(12.dp))

    MapCard(
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

    Spacer(Modifier.height(20.dp))

    Text(
        text = "Próximas paradas",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 20.dp),
    )

    Spacer(Modifier.height(4.dp))

    LazyColumn(modifier = Modifier.weight(1f)) {
      itemsIndexed(stops) { index, stop ->
        StopRow(
            stop = stop,
            index = index,
            totalStops = stops.size,
            currentStopIdx = currentStopIdx,
            eta = stopEtas.getOrNull(index),
        )
        if (index < stops.lastIndex) {
          HorizontalDivider(
              modifier = Modifier.padding(start = 68.dp, end = 20.dp),
              color = MaterialTheme.colorScheme.outline,
              thickness = 0.5.dp,
          )
        }
      }
      item { Spacer(Modifier.height(16.dp)) }
    }
  }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------
@Composable
private fun RouteHeader(routeName: String, onBackClick: () -> Unit) {
  ScreenHeader(
      title = "Ruta activa",
      subtitle = routeName.takeIf { it.isNotBlank() }?.trim()?.uppercase() ?: "CARGANDO RUTA...",
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
}

@Composable
private fun RouteStatusChips() {
  Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(999.dp)) {
      Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.tertiary, CircleShape))
        Text(
            text = "En transito",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            fontWeight = FontWeight.SemiBold,
        )
      }
    }

    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(999.dp)) {
      Text(
          text = "Actualizado ahora",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
      )
    }
  }
}

// ---------------------------------------------------------------------------
// Map card
// ---------------------------------------------------------------------------
@Composable
private fun MapCard(
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
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(210.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
          modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        ZoomControls(onZoomIn = onZoomIn, onZoomOut = onZoomOut)
        RecenterButton(onClick = onRecenter)
      }
    }
  }
}

// ---------------------------------------------------------------------------
// Stop row
// ---------------------------------------------------------------------------
@Composable
private fun StopRow(
    stop: RouteStop,
    index: Int,
    totalStops: Int,
    currentStopIdx: Int,
    eta: String? = null,
) {
  val isCompleted = index < currentStopIdx
  val isCurrent = index == currentStopIdx
  val isLast = index == totalStops - 1

  val statusText =
      when {
        isCompleted -> "Completada"
        isCurrent && isLast -> "Destino final · Llegando ahora"
        isCurrent -> "Llegando ahora"
        isLast -> "Destino final"
        else -> "En tránsito"
      }

  val statusColor = if (isCurrent) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant

  val badgeText = eta
  val badgeBg = if (isCurrent) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
  val badgeColor = if (isCurrent) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

  Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    // ── Timeline indicator ──────────────────────────────────────────────
    Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
      when {
        isCompleted -> {
          Box(
              modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.outline, CircleShape),
              contentAlignment = Alignment.Center,
          ) {
            Text("✓", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
          }
        }
        isCurrent -> {
          Box(
              modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
              contentAlignment = Alignment.Center,
          ) {
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.size(24.dp),
            ) {
              Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.size(11.dp).background(MaterialTheme.colorScheme.tertiary, CircleShape))
              }
            }
          }
        }
        else -> {
          Surface(
              shape = CircleShape,
              color = Color.Transparent,
              border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
              modifier = Modifier.size(32.dp),
          ) {}
        }
      }
    }

    Spacer(Modifier.width(16.dp))

    // ── Stop info ─────────────────────────────────────────────────────────
    Column(modifier = Modifier.weight(1f)) {
      Text(
          text = stop.name,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
          color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
          textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
          fontSize = if (isCurrent) 18.sp else 16.sp,
      )
      Spacer(Modifier.height(2.dp))
      Text(
          text = statusText,
          style = MaterialTheme.typography.bodySmall,
          color = statusColor,
          fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
      )
    }

    // ── Right badge ───────────────────────────────────────────────────────
    if (badgeText != null) {
      Surface(
          color = badgeBg,
          shape = RoundedCornerShape(12.dp),
          border = if (isCurrent) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelSmall,
            color = badgeColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
      }
    }
  }
}
