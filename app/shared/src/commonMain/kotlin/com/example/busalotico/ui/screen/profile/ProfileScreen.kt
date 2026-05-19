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
package com.example.busalotico.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.header.ScreenHeader
import com.example.busalotico.ui.components.input.SearchableCompanySelectField
import com.example.shared.domain.entities.Company

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    userRole: String,
    currentCompanyId: String? = null,
    companies: List<Company> = emptyList(),
    companiesLoading: Boolean = false,
    companyLoadError: String? = null,
    onUpdateCompany: ((String) -> Unit)? = null,
    onSignOut: () -> Unit,
    onOpenNotifications: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
  var selectedCompanyId by
      remember(currentCompanyId, companies) { mutableStateOf(currentCompanyId.orEmpty()) }
  val selectedCompanyName = companies.firstOrNull { it.id == selectedCompanyId }?.name.orEmpty()
  val showCompanyEditor = onUpdateCompany != null
  val showPassengerNotifications = userRole.equals("Pasajero", ignoreCase = true)
  val profileCardBackground = Color.White
  val inputBackground = Color(0xFFEEF2F5)
  val scrollState = rememberScrollState()

  Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Perfil",
          subtitle = "Información de la cuenta",
          trailingContent = {
            IconButton(onClick = onSignOut) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.Logout,
                  contentDescription = "Cerrar sesión",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          },
      )

      Column(
          modifier =
              Modifier.fillMaxWidth()
                  .weight(1f)
                  .verticalScroll(scrollState)
                  .padding(horizontal = 20.dp, vertical = 24.dp)
      ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
          Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = profileCardBackground,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = userName.ifBlank { "Usuario" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (userEmail.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                  text = userEmail,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }

            Spacer(modifier = Modifier.height(28.dp))

            ProfileInfoCard(
                label = "Nombre",
                value = userName.ifBlank { "-" },
                icon = Icons.Default.Person,
            )
            Spacer(modifier = Modifier.height(14.dp))
            ProfileInfoCard(
                label = "Rol",
                value = userRole.ifBlank { "-" },
                icon = Icons.Default.Badge,
            )
            Spacer(modifier = Modifier.height(14.dp))
            ProfileInfoCard(
                label = "Correo",
                value = userEmail.ifBlank { "-" },
                icon = Icons.Default.Email,
            )

            if (showPassengerNotifications) {
              Spacer(modifier = Modifier.height(18.dp))
              PillActionButton(
                  text = "Ver notificaciones",
                  onClick = onOpenNotifications,
                  modifier = Modifier.fillMaxWidth(),
                  leadingIcon = Icons.Default.Notifications,
              )
            }

            if (showCompanyEditor) {
              Spacer(modifier = Modifier.height(22.dp))

              Surface(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(24.dp),
                  color = profileCardBackground,
                  border =
                      BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
              ) {
                Column(
                    modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                  Row(
                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                      verticalAlignment = Alignment.Top,
                  ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                      }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                      Text(
                          text = "Información empresarial",
                          style = MaterialTheme.typography.labelSmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                          fontWeight = FontWeight.SemiBold,
                      )
                      selectedCompanyName
                          .takeIf { it.isNotBlank() }
                          ?.let { companyName ->
                            Text(
                                text = companyName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold,
                            )
                          }
                    }
                  }

                  selectedCompanyName
                      .takeIf { it.isNotBlank() }
                      ?.let {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                          Box(
                              modifier =
                                  Modifier.size(7.dp).background(Color(0xFF37B24D), CircleShape)
                          )
                          Text(
                              text = "Verificada",
                              style = MaterialTheme.typography.labelSmall,
                              color = MaterialTheme.colorScheme.onSurfaceVariant,
                              fontWeight = FontWeight.SemiBold,
                          )
                        }
                      }

                  SearchableCompanySelectField(
                      value = selectedCompanyName,
                      placeholder = if (companiesLoading) "Cargando empresas..." else "Empresa",
                      enabled = companies.isNotEmpty() && !companiesLoading,
                      companies = companies,
                      onSelect = { company -> selectedCompanyId = company.id },
                      containerColor = inputBackground,
                      border = null,
                  )

                  companyLoadError?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                  }

                  PillActionButton(
                      text = "Guardar empresa",
                      enabled =
                          selectedCompanyId.isNotBlank() &&
                              selectedCompanyId != currentCompanyId &&
                              !companiesLoading,
                      onClick = { onUpdateCompany?.invoke(selectedCompanyId) },
                      modifier = Modifier.fillMaxWidth(),
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ProfileInfoCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(22.dp),
      color = Color(0xFFEEF2F5),
      border = null,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)) {
      Text(
          text = label.uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
      )
      Spacer(modifier = Modifier.height(10.dp))
      Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
      }
    }
  }
}
