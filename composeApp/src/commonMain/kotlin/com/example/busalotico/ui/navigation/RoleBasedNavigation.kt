package com.example.busalotico.ui.navigation

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.entities.UserRole

@Composable
fun RoleBasedNavigation(
    role: UserRole,
    coordinate: Coordinate?,
    userId: String,
    userName: String,
    userEmail: String,
    userRoleLabel: String,
    onSignOut: () -> Unit,
) {
  when (role) {
    UserRole.PASSENGER ->
        BusAppNavigation(
            coordinate = coordinate,
            userId = userId,
            userName = userName,
            userEmail = userEmail,
            userRole = userRoleLabel,
            onSignOut = onSignOut,
        )
    UserRole.DRIVER ->
        DriverNavigation(
            userId = userId,
            userName = userName,
            userEmail = userEmail,
            userRole = userRoleLabel,
            onSignOut = onSignOut,
        )
    UserRole.ADMIN ->
        AdminNavigation(
            userName = userName,
            userEmail = userEmail,
            userRole = userRoleLabel,
            onSignOut = onSignOut,
        )
  }
}
