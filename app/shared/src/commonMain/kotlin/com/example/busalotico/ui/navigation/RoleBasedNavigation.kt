/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
