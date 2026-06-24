/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.busalotico.ui.navigation

import androidx.compose.runtime.Composable
import com.example.sharedlogic.domain.entities.Company
import com.example.sharedlogic.domain.entities.Coordinate
import com.example.sharedlogic.domain.entities.UserRole

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
