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

// Modelo de apoyo para los datos de la lista
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
    // Koin nos da la misma instancia 'single' que usamos en la navegación
    viewModel: RouteDetailViewModel = koinViewModel()
) {
    // 1. OBSERVAMOS EL ESTADO (La línea azul que se cargó al hacer clic)
    val polylinePoints by viewModel.polylinePoints.collectAsState()
    val busLocation by viewModel.busLocation.collectAsState()

    // Triggers para el control del mapa
    var recenterTrigger by remember { mutableIntStateOf(0) }
    var zoomInTrigger by remember { mutableIntStateOf(0) }
    var zoomOutTrigger by remember { mutableIntStateOf(0) }

    // Lista de paradas (Basura temporal que coincide con los puntos de prueba del NavHost)
    val detailedStops = remember(routeId) {
        listOf(
            BusStopDetail(RouteStop("1", "Terminal San José",  9.984506, -84.729457, 1), "08:15", "Completada",       isCompleted = true),
            BusStopDetail(RouteStop("2", "La Sabana",          9.982033, -84.734038, 2), "08:27", "Llegando ahora",   isCurrent = true),
            BusStopDetail(RouteStop("3", "Hospital México",    9.979011, -84.734060, 3), "08:39", "En 12 mins aprox"),
            BusStopDetail(RouteStop("4", "Río Segundo",        9.978060, -84.735616, 4), "08:45", "En 18 mins aprox"),
            BusStopDetail(RouteStop("5", "San Antonio",        9.980385, -84.735727, 5), "08:51", "En 24 mins aprox"),
            BusStopDetail(RouteStop("6", "La Uruca",           9.981832, -84.735448, 6), "08:57", "En 30 mins aprox"),
            BusStopDetail(RouteStop("7", "La Garita",          9.983544, -84.735658, 7), "09:03", "En 36 mins aprox"),
            BusStopDetail(RouteStop("8", "San Joaquín",        9.984326, -84.734534, 8), "09:09", "En 42 mins aprox"),
            BusStopDetail(RouteStop("9", "Ciruelas",           9.985152, -84.732383, 9), "09:15", "En 48 mins aprox"),
            BusStopDetail(RouteStop("10", "Alajuela Central",  9.986002, -84.730219, 10), "09:10", "Destino Final"),
            BusStopDetail(RouteStop("1", "Terminal San José",  9.984506, -84.729457, 1), "08:15", "Completada",       isCompleted = true),
        )
    }

    // Centrado automático cuando cambia el GPS del usuario
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
            text = "Detalle de Ruta: $routeId", // Ahora usa el ID real seleccionado
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
                    stops = detailedStops.map { it.baseStop },
                    // PASAMOS LA LÍNEA QUE VIENE DEL VIEWMODEL COMPARTIDO
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

// ... (StatusBadge y StopItem se mantienen igual ya que son UI pura)
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