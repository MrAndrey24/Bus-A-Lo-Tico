package com.example.busalotico.ui.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.busalotico.ui.components.mapview.details.MapViewDetail
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.route.RouteDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

// ---------------------------------------------------------------------------
// Colors
// ---------------------------------------------------------------------------
private val GreenActive  = Color(0xFF2E7D32)
private val GreenLight   = Color(0xFFE8F5E9)
private val GreenDot     = Color(0xFF4CAF50)
private val GreyText     = Color(0xFF9E9E9E)
private val GreySurface  = Color(0xFFF5F5F5)
private val GreyLine     = Color(0xFFE0E0E0)
private val TextPrimary  = Color(0xFF1A1A1A)
private val BlueLight    = Color(0xFFE3F2FD)
private val BlueText     = Color(0xFF1565C0)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------
@Composable
fun RouteDetailScreen(
    routeId: String,
    coordinate: Coordinate,
    viewModel: RouteDetailViewModel = koinViewModel()
) {
    val polylinePoints by viewModel.polylinePoints.collectAsState()
    val stops          by viewModel.stops.collectAsState()
    val busLocation    by viewModel.busLocation.collectAsState()
    val routeName      by viewModel.routeName.collectAsState()
    val currentStopIdx by viewModel.currentStopIndex.collectAsState()
    val stopEtas        by viewModel.stopEtas.collectAsState()

    var recenterTrigger by remember { mutableIntStateOf(0) }
    var zoomInTrigger   by remember { mutableIntStateOf(0) }
    var zoomOutTrigger  by remember { mutableIntStateOf(0) }

    LaunchedEffect(coordinate) {
        if (coordinate.latitude != 0.0) recenterTrigger++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RouteHeader(routeName = routeName)

        MapCard(
            recenterTrigger = recenterTrigger,
            zoomInTrigger   = zoomInTrigger,
            zoomOutTrigger  = zoomOutTrigger,
            stops           = stops,
            polylinePoints  = polylinePoints,
            busLocation     = busLocation,
            onZoomIn        = { zoomInTrigger++ },
            onZoomOut       = { zoomOutTrigger++ },
            onRecenter      = { recenterTrigger++ }
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text       = "Próximas paradas",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(4.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(stops) { index, stop ->
                StopRow(
                    stop           = stop,
                    index          = index,
                    totalStops     = stops.size,
                    currentStopIdx = currentStopIdx,
                    eta            = stopEtas.getOrNull(index)
                )
                if (index < stops.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(start = 68.dp, end = 20.dp),
                        color     = GreyLine,
                        thickness = 0.5.dp
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
private fun RouteHeader(routeName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text          = "RUTA ACTIVA",
            style         = MaterialTheme.typography.labelSmall,
            color         = GreyText,
            letterSpacing = 1.2.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text       = routeName.ifBlank { "Cargando ruta..." },
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip(
                text      = "En tránsito",
                dotColor  = GreenDot,
                bgColor   = GreenLight,
                textColor = GreenActive
            )
            StatusChip(
                text      = "Actualizado ahora",
                bgColor   = GreySurface,
                textColor = GreyText
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
    onRecenter: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(210.dp),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            MapViewDetail(
                modifier        = Modifier.fillMaxSize(),
                recenterTrigger = recenterTrigger,
                zoomInTrigger   = zoomInTrigger,
                zoomOutTrigger  = zoomOutTrigger,
                stops           = stops,
                polylinePoints  = polylinePoints,
                busLocation     = busLocation
            )
            Column(
                modifier            = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
    eta: String? = null
) {
    val isCompleted = index < currentStopIdx
    val isCurrent   = index == currentStopIdx
    val isLast      = index == totalStops - 1

    val statusText = when {
        isCompleted -> "Completada"
        isCurrent && isLast -> "Destino final · Llegando ahora"
        isCurrent   -> "Llegando ahora"
        isLast      -> "Destino final"
        else        -> "En tránsito"
    }

    val statusColor = if (isCurrent) GreenDot else GreyText

    val badgeText  = eta
    val badgeBg    = if (isCurrent) GreenLight else GreySurface
    val badgeColor = if (isCurrent) GreenActive else GreyText

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Timeline indicator ──────────────────────────────────────────────
        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            when {
                isCompleted -> {
                    Box(
                        modifier         = Modifier
                            .size(32.dp)
                            .background(GreyLine, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 13.sp, color = GreyText, fontWeight = FontWeight.Bold)
                    }
                }
                isCurrent -> {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(GreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape    = CircleShape,
                            color    = Color.Transparent,
                            border   = androidx.compose.foundation.BorderStroke(2.dp, GreenDot),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier         = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(11.dp)
                                        .background(GreenDot, CircleShape)
                                )
                            }
                        }
                    }
                }
                else -> {
                    Surface(
                        shape    = CircleShape,
                        color    = Color.Transparent,
                        border   = androidx.compose.foundation.BorderStroke(1.5.dp, GreyLine),
                        modifier = Modifier.size(32.dp)
                    ) {}
                }
            }
        }

        Spacer(Modifier.width(16.dp))

        // ── Stop info ─────────────────────────────────────────────────────────
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text           = stop.name,
                style          = MaterialTheme.typography.bodyLarge,
                fontWeight     = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                color          = if (isCompleted) GreyText else TextPrimary,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                fontSize       = if (isCurrent) 18.sp else 16.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text  = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = statusColor,
                fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        // ── Right badge ───────────────────────────────────────────────────────
        if (badgeText != null) {
            Surface(
                color  = badgeBg,
                shape  = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text     = badgeText,
                    style    = MaterialTheme.typography.labelSmall,
                    color    = badgeColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Status chip
// ---------------------------------------------------------------------------
@Composable
private fun StatusChip(
    text: String,
    bgColor: Color,
    textColor: Color,
    dotColor: Color? = null
) {
    Surface(color = bgColor, shape = RoundedCornerShape(20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            if (dotColor != null) {
                Box(modifier = Modifier.size(7.dp).background(dotColor, CircleShape))
                Spacer(Modifier.width(5.dp))
            }
            Text(text = text, style = MaterialTheme.typography.labelMedium, color = textColor)
        }
    }
}