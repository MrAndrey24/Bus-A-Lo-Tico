package com.example.busalotico.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.domain.entities.Company
import com.example.shared.domain.entities.Route

@Composable
fun RouteDebugScreen(
    company: Company?,
    route: Route?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Route Debug Screen")
        Text("Company ID: ${company?.id ?: "null"}")
        Text("Company Name: ${company?.name ?: "null"}")
        Text("Route ID: ${route?.id ?: "null"}")
        Text("Route Name: ${route?.name ?: "null"}")
        Text("Start Latitude: ${route?.startLatitude ?: "null"}")
        Text("Start Longitude: ${route?.startLongitude ?: "null"}")
        Text("End Latitude: ${route?.endLatitude ?: "null"}")
        Text("End Longitude: ${route?.endLongitude ?: "null"}")
        Text("Stops:")
        route?.stops?.forEach { stop ->
            Text("- ${stop.order}: ${stop.name} (${stop.latitude}, ${stop.longitude})")
        } ?: Text("- none")
        Text("Schedules:")
        route?.schedules?.forEach { schedule ->
            Text("- ${schedule.id}: driver=${schedule.driverId}, ${schedule.departureTime}-${schedule.arrivalTime}")
        } ?: Text("- none")

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}
