/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.busalotico.ui.screens.desktop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.DriverFormState
import com.example.busalotico.ui.components.admin.AdminTextField
import com.example.busalotico.ui.components.desktop.DesktopListCard
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.presentation.admin.AdminCompanyUiState

@Composable
fun DriverManagementSection(
    uiState: AdminCompanyUiState,
    driverForm: DriverFormState,
    onDriverFormChange: (DriverFormState) -> Unit,
    onSelectDriver: (Driver) -> Unit,
    onClearDriverForm: () -> Unit,
    onSaveDriver: () -> Unit,
    onDeleteDriver: () -> Unit,
) {
  var passwordVisible by remember { mutableStateOf(false) }

  Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
    ElevatedCard(
        modifier = Modifier.width(360.dp).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxSize().padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
      ) {
        Text(
            "Conductores",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text =
                uiState.selectedRoute?.companyId?.let { companyId ->
                  uiState.companyNames[companyId] ?: companyId
                } ?: "Selecciona una ruta con empresa asignada para ver conductores.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFEAECEF))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(uiState.companyDrivers, key = { it.id }) { driver ->
            DesktopListCard(
                title = driver.name,
                subtitle = driver.id,
                icon = Icons.Default.Person,
                selected = driverForm.id == driver.id,
                onClick = { onSelectDriver(driver) },
            )
          }
        }
      }
    }
    ElevatedCard(
        modifier = Modifier.weight(1f).fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(18.dp),
      ) {
        Text(
            "Ficha de conductor",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        AdminTextField(
            "Nombre",
            driverForm.name,
            { onDriverFormChange(driverForm.copy(name = it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        AdminTextField(
            "Correo electrónico",
            driverForm.email,
            { onDriverFormChange(driverForm.copy(email = it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = driverForm.password,
            onValueChange = { onDriverFormChange(driverForm.copy(password = it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
              Icon(
                  imageVector =
                      if (passwordVisible) Icons.Outlined.Visibility
                      else Icons.Outlined.VisibilityOff,
                  contentDescription =
                      if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                  modifier = Modifier.clickable { passwordVisible = !passwordVisible },
              )
            },
        )
        OutlinedTextField(
            value = uiState.selectedRoute?.companyId.orEmpty(),
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Empresa vinculada") },
            supportingText = {
              Text(
                  uiState.selectedRoute?.companyId?.let { uiState.companyNames[it] ?: it }
                      ?: "Selecciona una ruta para asignar empresa.",
              )
            },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          TextButton(onClick = onClearDriverForm, enabled = !uiState.isSavingDriver) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo conductor")
            Spacer(Modifier.width(6.dp))
            Text("Nuevo")
          }
          Button(
              onClick = onSaveDriver,
              enabled =
                  !uiState.isSavingDriver &&
                      driverForm.name.isNotBlank() &&
                      driverForm.email.isNotBlank() &&
                      driverForm.password.isNotBlank() &&
                      uiState.selectedRoute?.companyId != null,
              shape = RoundedCornerShape(16.dp),
          ) {
            if (uiState.isSavingDriver) {
              CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
              Icon(Icons.Default.Save, contentDescription = "Guardar conductor")
            }
          }
          if (driverForm.id.isNotBlank()) {
            TextButton(onClick = onDeleteDriver, enabled = !uiState.isSavingDriver) {
              Icon(Icons.Default.Delete, contentDescription = "Eliminar conductor")
            }
          }
        }
      }
    }
  }
}
