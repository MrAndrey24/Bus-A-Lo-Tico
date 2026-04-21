/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.navigation

import androidx.compose.runtime.Composable
import com.example.shared.domain.entities.Company
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
    companyId: String?,
    companies: List<Company>,
    companiesLoading: Boolean,
    companyLoadError: String?,
    onUpdateCompany: (String) -> Unit,
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
            coordinate = coordinate,
            userId = userId,
            userName = userName,
            userEmail = userEmail,
            userRole = userRoleLabel,
            companyId = companyId,
            companies = companies,
            companiesLoading = companiesLoading,
            companyLoadError = companyLoadError,
            onUpdateCompany = onUpdateCompany,
            onSignOut = onSignOut,
        )
    UserRole.ADMIN ->
        AdminNavigation(
            userName = userName,
            userEmail = userEmail,
            userRole = userRoleLabel,
            companyId = companyId,
            companies = companies,
            companiesLoading = companiesLoading,
            companyLoadError = companyLoadError,
            onUpdateCompany = onUpdateCompany,
            onSignOut = onSignOut,
        )
  }
}
