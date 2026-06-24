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
package com.example.busalotico.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.AdminSection

@Composable
fun DesktopCenteredLoading() {
  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF6F7FB)),
      contentAlignment = Alignment.Center,
  ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      CircularProgressIndicator(color = Color(0xFF0F2746))
      Text("Cargando sesión...", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
fun DesktopFeedbackCard(
    message: String,
    background: Color,
    content: Color,
    onDismiss: () -> Unit,
) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .background(background, RoundedCornerShape(18.dp))
              .padding(horizontal = 18.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(message, color = content, style = MaterialTheme.typography.bodyMedium)
    TextButton(onClick = onDismiss) { Text("Ocultar", color = content) }
  }
}

@Composable
fun EmptyPanelState(message: String) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}

@Composable
fun DesktopListCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val hovered by interactionSource.collectIsHoveredAsState()
  val containerColor =
      when {
        selected -> Color(0xFFEEF4FF)
        hovered -> Color(0xFFF2F6FD)
        else -> Color(0xFFF9FAFC)
      }
  val borderColor =
      when {
        selected -> Color(0xFFD6E4FF)
        hovered -> Color(0xFFE1E8F5)
        else -> Color(0xFFF9FAFC)
      }

  Card(
      modifier =
          Modifier.fillMaxWidth()
              .hoverable(interactionSource = interactionSource)
              .clickable(onClick = onClick),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = containerColor),
      border = BorderStroke(1.dp, borderColor),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Surface(
          modifier = Modifier.size(40.dp),
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
              imageVector = icon,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
fun AdminTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
) {
  OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = modifier.widthIn(min = 220.dp),
      label = { Text(label) },
      supportingText =
          if (supportingText != null) {
            { Text(supportingText) }
          } else {
            null
          },
      singleLine = true,
  )
}

@Composable
fun AdminDropdownField(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    enabled: Boolean = true,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
  var expanded by remember(options, value) { mutableStateOf(false) }

  Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier.widthIn(min = 220.dp),
  ) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Box {
      Surface(
          modifier =
              Modifier.fillMaxWidth().clickable(enabled = enabled && options.isNotEmpty()) {
                expanded = !expanded
              },
          shape = RoundedCornerShape(16.dp),
          color = if (enabled) Color.White else Color(0xFFF9FAFC),
          border = BorderStroke(1.dp, Color(0xFFE3E8F0)),
      ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text =
                  value.ifBlank {
                    if (!enabled || options.isEmpty()) "Sin opciones disponibles" else placeholder
                  },
              style = MaterialTheme.typography.bodyLarge,
              color =
                  if (value.isBlank()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                  } else {
                    MaterialTheme.colorScheme.onSurface
                  },
          )
          Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
      DropdownMenu(
          expanded = expanded && enabled && options.isNotEmpty(),
          onDismissRequest = { expanded = false },
          modifier = Modifier.width(320.dp).background(Color.White),
      ) {
        options.forEach { option ->
          DropdownMenuItem(
              text = {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (value == option) FontWeight.SemiBold else FontWeight.Normal,
                )
              },
              onClick = {
                onSelect(option)
                expanded = false
              },
          )
        }
      }
    }
  }
}

@Composable
fun DesktopSidebar(
    selectedSection: AdminSection,
    onSectionSelected: (AdminSection) -> Unit,
) {
  Surface(
      modifier = Modifier.width(260.dp).fillMaxHeight(),
      color = Color(0xFF0F2746),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      Text(
          text = "Bus A Lo Tico",
          style = MaterialTheme.typography.headlineSmall,
          color = Color.White,
          fontWeight = FontWeight.Bold,
      )
      Text(
          text = "Gestión empresarial",
          style = MaterialTheme.typography.bodyMedium,
          color = Color.White.copy(alpha = 0.75f),
      )
      Divider(color = Color.White.copy(alpha = 0.15f))
      SidebarItem(
          "Resumen",
          Icons.Default.DirectionsBus,
          selectedSection == AdminSection.DASHBOARD,
      ) {
        onSectionSelected(AdminSection.DASHBOARD)
      }
      SidebarItem("Rutas", Icons.Default.LocationOn, selectedSection == AdminSection.ROUTES) {
        onSectionSelected(AdminSection.ROUTES)
      }
      SidebarItem("Conductores", Icons.Default.Person, selectedSection == AdminSection.DRIVERS) {
        onSectionSelected(AdminSection.DRIVERS)
      }
    }
  }
}

@Composable
private fun SidebarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .background(
                  color = if (selected) Color.White.copy(alpha = 0.14f) else Color.Transparent,
                  shape = RoundedCornerShape(16.dp),
              )
              .clickable(onClick = onClick)
              .padding(horizontal = 16.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(icon, contentDescription = null, tint = Color.White)
    Text(
        label,
        color = Color.White,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
    )
  }
}

@Composable
fun DesktopHeader(
    title: String,
    subtitle: String,
    currentUserName: String,
    currentCompanyLabel: String,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit,
) {
  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
      Text(
          subtitle,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      if (currentUserName.isNotBlank() || currentCompanyLabel.isNotBlank()) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
          if (currentUserName.isNotBlank()) {
            Text(
                text = currentUserName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
          }
          if (currentCompanyLabel.isNotBlank()) {
            Text(
                text = currentCompanyLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
      IconButton(onClick = onRefresh) {
        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
      }
      IconButton(onClick = onSignOut) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
      }
    }
  }
}
