package com.example.busalotico.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.RecenterButton
import com.example.busalotico.ui.components.ZoomControls
import com.example.busalotico.ui.components.mapview.details.MapViewDetail
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.route.RouteDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

data class BusStopDetail(
    val baseStop: RouteStop,
    val time: String,
    val status: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

@Composable
fun RouteDetailScreen(
    routeId: String,
    coordinate: Coordinate,
    // Koin
    viewModel: RouteDetailViewModel = koinViewModel()
) {
    val polylinePoints by viewModel.polylinePoints.collectAsState()
    val realStops by viewModel.stops.collectAsState()
    val busLocation by viewModel.busLocation.collectAsState()

    // Triggers for map control
    var recenterTrigger by remember { mutableIntStateOf(0) }
    var zoomInTrigger by remember { mutableIntStateOf(0) }
    var zoomOutTrigger by remember { mutableIntStateOf(0) }


    val detailedStops = remember(routeId) {
        realStops.mapIndexed { index, routeStop ->
            BusStopDetail(
                baseStop = routeStop,
                // Later, connect it to real-time schedules in Firebase
                time = "12:00",
                status = when {
                    index == 0 -> "Completada"
                    index == 1 -> "Llegando ahora"
                    index == realStops.lastIndex -> "Destino Final"
                    else -> "En tránsito"
                },
                isCompleted = index <= 0,
                isCurrent = index == 1
            )
        }
    }

    // Automatically centers when the user's GPS location changes
    LaunchedEffect(coordinate) {
        if (coordinate.latitude != 0.0) {
            recenterTrigger++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de Ruta: $routeId", // Real ID selected
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            StatusBadge(text = "En tránsito", color = Color(0xFFE8F5E9), textColor = Color(0xFF2E7D32))
            Spacer(Modifier.width(8.dp))
            StatusBadge(text = "Actualizado ahora", color = Color.White, textColor = Color.Gray, border = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN DEL MAPA ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MapViewDetail(
                    modifier = Modifier.fillMaxSize(),
                    recenterTrigger = recenterTrigger,
                    zoomInTrigger = zoomInTrigger,
                    zoomOutTrigger = zoomOutTrigger,
                    stops = realStops,
                    polylinePoints = polylinePoints,
                    busLocation = busLocation
                )

                // Controles de Mapa
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ZoomControls(
                        onZoomIn = { zoomInTrigger++ },
                        onZoomOut = { zoomOutTrigger++ }
                    )
                    RecenterButton(
                        onClick = { recenterTrigger++ }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Próximas paradas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(detailedStops) { item ->
                StopItem(
                    name = item.baseStop.name,
                    time = item.time,
                    status = item.status,
                    isCompleted = item.isCompleted,
                    isCurrent = item.isCurrent
                )
            }
        }
    }
}

@Composable
fun StatusBadge(text: String, color: Color, textColor: Color, border: Boolean = false) {
    Surface(
        color = color,
        shape = RoundedCornerShape(20.dp),
        border = if (border) BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            if (!border) Box(modifier = Modifier.size(8.dp).background(textColor, CircleShape))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text, color = textColor, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun StopItem(name: String, time: String, status: String, isCompleted: Boolean = false, isCurrent: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(2.dp, if (isCurrent) Color(0xFF4CAF50) else Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) Text("✓", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                if (isCurrent) Box(modifier = Modifier.size(12.dp).background(Color(0xFF4CAF50), CircleShape))
            }
            Box(modifier = Modifier.weight(1f).width(2.dp).background(Color.LightGray))
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = status,
                color = if (isCurrent) Color(0xFF4CAF50) else Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(text = time, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}