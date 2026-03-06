package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.busalotico.ui.screen.home.HomeScreen
import com.example.shared.domain.entities.Coordinate

@Composable
fun BusAppNavigation(
    coordinate: Coordinate? // Recibimos la ubicación que viene del ViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BusAppScreen.Home.name

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Home.name,
                    onClick = { navController.navigate(BusAppScreen.Home.name) },
                    label = { Text("Mapa") },
                    icon = { Text("📍") }
                )
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Routes.name,
                    onClick = { navController.navigate(BusAppScreen.Routes.name) },
                    label = { Text("Rutas") },
                    icon = { Text("🚌") }
                )
                NavigationBarItem(
                    selected = currentRoute == BusAppScreen.Detail.name,
                    onClick = { navController.navigate(BusAppScreen.Detail.name) },
                    label = { Text("Detalle") },
                    icon = { Text("📋") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BusAppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BusAppScreen.Home.name) {
                // LÓGICA DE SEGURIDAD: Solo entramos si hay coordenadas
                val currentCoord = coordinate
                if (currentCoord != null) {
                    HomeScreen(
                        coordinate = currentCoord, // Aquí ya no es nulo, error resuelto
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Si es nulo, ponemos un mensaje simple para no quebrar la app
                    Text("Buscando señal GPS...")
                }
            }

            composable(route = BusAppScreen.Routes.name) {
                Text("Pantalla de Rutas")
            }

            composable(route = BusAppScreen.Detail.name) {
                Text("Pantalla de Detalle")
            }
        }
    }
}