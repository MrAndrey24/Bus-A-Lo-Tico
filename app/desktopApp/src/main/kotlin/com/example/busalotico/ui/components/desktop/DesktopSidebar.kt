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
package com.example.busalotico.ui.components.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.AdminSection
import com.example.busalotico.ui.components.SidebarItem

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
