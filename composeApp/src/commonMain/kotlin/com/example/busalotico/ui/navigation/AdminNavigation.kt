package com.example.busalotico.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
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
import com.example.busalotico.ui.screen.profile.ProfileScreen
import com.example.shared.domain.entities.Company

@Composable
fun AdminNavigation(
    userName: String,
    userEmail: String,
    userRole: String,
    companyId: String?,
    companies: List<Company>,
    companiesLoading: Boolean,
    companyLoadError: String?,
    onUpdateCompany: (String) -> Unit,
    onSignOut: () -> Unit,
) {
  val navController = rememberNavController()
  val backStackEntry = navController.currentBackStackEntryAsState().value
  val currentRoute = backStackEntry?.destination?.route ?: AdminScreen.Company.name

  Scaffold(
      bottomBar = {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
          AdminScreen.entries.forEach { screen ->
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
        startDestination = AdminScreen.Company.name,
        modifier = Modifier.padding(innerPadding),
    ) {
      composable(AdminScreen.Company.name) {
        RolePlaceholderScreen(
            title = "Empresa",
            subtitle = "Espacio para gestion administrativa de la empresa",
        )
      }
      composable(AdminScreen.Drivers.name) {
        RolePlaceholderScreen(
            title = "Choferes",
            subtitle = "Espacio para gestion de conductores",
        )
      }
      composable(AdminScreen.Routes.name) {
        RolePlaceholderScreen(
            title = "Rutas",
            subtitle = "Espacio para gestion administrativa de rutas",
        )
      }
      composable(AdminScreen.Profile.name) {
        ProfileScreen(
            userName = userName,
            userEmail = userEmail,
            userRole = userRole,
            currentCompanyId = companyId,
            companies = companies,
            companiesLoading = companiesLoading,
            companyLoadError = companyLoadError,
            onUpdateCompany = onUpdateCompany,
            onSignOut = onSignOut,
        )
      }
    }
  }
}

private enum class AdminScreen(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
  Company("Empresa", Icons.Default.Business),
  Drivers("Choferes", Icons.Default.Group),
  Routes("Rutas", Icons.Default.DirectionsBus),
  Profile("Perfil", Icons.Default.Person),
}
