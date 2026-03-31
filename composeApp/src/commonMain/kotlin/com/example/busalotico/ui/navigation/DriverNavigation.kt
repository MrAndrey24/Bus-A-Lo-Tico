package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.busalotico.ui.screen.driver.DriverHomeScreen
import com.example.busalotico.ui.screen.profile.ProfileScreen
import com.example.shared.domain.entities.Coordinate

@Composable
fun DriverNavigation(
    coordinate: Coordinate?,
    userId: String,
    userName: String,
    userEmail: String,
    userRole: String,
    onSignOut: () -> Unit,
) {
  val navController = rememberNavController()
  val backStackEntry = navController.currentBackStackEntryAsState().value
  val currentRoute = backStackEntry?.destination?.route ?: DriverScreen.MyRoute.name

  Scaffold(
      bottomBar = {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
          DriverScreen.entries.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.name,
                onClick = { navController.navigate(screen.name) },
                label = { Text(screen.label) },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
            )
          }
        }
      }
  ) { innerPadding ->
    NavHost(
        navController = navController,
        startDestination = DriverScreen.MyRoute.name,
        modifier = Modifier.padding(innerPadding),
    ) {
      composable(DriverScreen.MyRoute.name) {
        DriverHomeScreen(
            coordinate = coordinate,
            userId = userId,
            userName = userName,
        )
      }
      composable(DriverScreen.Profile.name) {
        ProfileScreen(
            userName = userName,
            userEmail = userEmail,
            userRole = userRole,
            onSignOut = onSignOut,
        )
      }
    }
  }
}

private enum class DriverScreen(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
  MyRoute("Mi ruta", Icons.Default.Route),
  Profile("Perfil", Icons.Default.Person),
}
