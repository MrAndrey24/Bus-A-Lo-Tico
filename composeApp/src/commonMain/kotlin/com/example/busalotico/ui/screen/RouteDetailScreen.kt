package com.example.busalotico.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.mapview.MapViewComponent // Componente reutilizado
import com.example.shared.domain.entities.RouteStop
import com.example.shared.presentation.LocationViewModel
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
    locationViewModel: LocationViewModel = koinViewModel()
) {
    // 1. Estado de ubicación global
    val userCoordinate by locationViewModel.coordinate.collectAsState()

    // 2. Triggers replicados del HomeScreen para el control del mapa
    var recenterTrigger by remember { mutableStateOf(0) }
    var zoomInTrigger by remember { mutableStateOf(0) }
    var zoomOutTrigger by remember { mutableStateOf(0) }

    // Disparamos el centrado inicial cuando carga la coordenada
    LaunchedEffect(userCoordinate) {
        recenterTrigger++
    }

    // Lista de paradas quemada para pruebas
    val detailedStops = remember(routeId) {
        listOf(
            BusStopDetail(RouteStop("1", "Terminal San José", 9.9347, -84.0797, 1), "08:15", "Completada", isCompleted = true),
            BusStopDetail(RouteStop("2", "La Sabana", 9.9358, -84.1021, 2), "08:27", "Llegando ahora", isCurrent = true),
            BusStopDetail(RouteStop("3", "Hospital México", 9.9515, -84.1147, 3), "08:39", "En 12 mins aprox"),
            BusStopDetail(RouteStop("4", "Alajuela Central", 10.0163, -84.2138, 4), "09:10", "Destino Final")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // --- HEADER ---
        Text(text = "San José — Alajuela", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            StatusBadge(text = "En tránsito", color = Color(0xFFE8F5E9), textColor = Color(0xFF2E7D32))
            Spacer(Modifier.width(8.dp))
            StatusBadge(text = "Actualizado hace 1 min", color = Color.White, textColor = Color.Gray, border = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- EL MAPA: RÉPLICA EXACTA DEL HOME ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MapViewComponent(
                    coordinate = userCoordinate,
                    recenterTrigger = recenterTrigger,
                    zoomInTrigger = zoomInTrigger,
                    zoomOutTrigger = zoomOutTrigger
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Próximas paradas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        // --- LISTA DE PARADAS ---
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
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
            Text(text = status, color = if (isCurrent) Color(0xFF4CAF50) else Color.Gray, style = MaterialTheme.typography.bodyMedium)
        }
        Text(text = time, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}