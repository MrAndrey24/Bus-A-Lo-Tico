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
package com.example.busalotico.ui.screens.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.model.RouteFormState
import com.example.busalotico.ui.components.admin.AdminDropdownField
import com.example.busalotico.ui.components.admin.AdminTextField
import com.example.sharedlogic.domain.entities.geo.costaRicaGeoData

@Composable
fun RouteDetailsEditor(
    routeForm: RouteFormState,
    onRouteFormChange: (RouteFormState) -> Unit,
    companyLabel: String,
    onSaveRoute: () -> Unit,
    isSaving: Boolean,
) {
  val provinces = remember {
    costaRicaGeoData()
        .sortedBy { it.name.lowercase() }
        .map { province ->
          province.copy(
              cantons =
                  province.cantons
                      .sortedBy { it.name.lowercase() }
                      .map { canton ->
                        canton.copy(districts = canton.districts.sortedBy { it.lowercase() })
                      },
          )
        }
  }
  val startProvince = provinces.firstOrNull { it.name == routeForm.startProvince }
  val startCantons = startProvince?.cantons ?: emptyList()
  val startCanton = startCantons.firstOrNull { it.name == routeForm.startCanton }
  val startDistricts = startCanton?.districts ?: emptyList()

  val endProvince = provinces.firstOrNull { it.name == routeForm.endProvince }
  val endCantons = endProvince?.cantons ?: emptyList()
  val endCanton = endCantons.firstOrNull { it.name == routeForm.endCanton }
  val endDistricts = endCanton?.districts ?: emptyList()

  val scrollState = rememberScrollState()
  Column(
      modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(18.dp),
  ) {
    Text(
        "Datos de la ruta",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Código",
          routeForm.code,
          { onRouteFormChange(routeForm.copy(code = it)) },
          modifier = Modifier.weight(0.32f),
      )
      AdminTextField(
          "Nombre",
          routeForm.name,
          { onRouteFormChange(routeForm.copy(name = it)) },
          modifier = Modifier.weight(0.68f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Operador",
          routeForm.operator,
          { onRouteFormChange(routeForm.copy(operator = it)) },
          modifier = Modifier.weight(0.55f),
      )
      AdminTextField(
          "Empresa (ID)",
          routeForm.companyId,
          { onRouteFormChange(routeForm.copy(companyId = it)) },
          supportingText = companyLabel,
          modifier = Modifier.weight(0.45f),
      )
    }

    Text(
        "Punto de inicio",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminDropdownField(
          label = "Provincia inicio",
          value = routeForm.startProvince,
          placeholder = "Selecciona una provincia",
          options = provinces.map { it.name },
          onSelect = { provinceName ->
            onRouteFormChange(
                routeForm.copy(
                    startProvince = provinceName,
                    startCanton = "",
                    startDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Cantón inicio",
          value = routeForm.startCanton,
          placeholder = "Selecciona un cantón",
          options = startCantons.map { it.name },
          enabled = startCantons.isNotEmpty(),
          onSelect = { cantonName ->
            onRouteFormChange(
                routeForm.copy(
                    startCanton = cantonName,
                    startDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Distrito inicio",
          value = routeForm.startDistrict,
          placeholder = "Selecciona un distrito",
          options = startDistricts,
          enabled = startDistricts.isNotEmpty(),
          onSelect = { districtName ->
            onRouteFormChange(routeForm.copy(startDistrict = districtName))
          },
          modifier = Modifier.weight(1f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Tarifa CRC",
          routeForm.fareCrc,
          { onRouteFormChange(routeForm.copy(fareCrc = it)) },
          modifier = Modifier.weight(0.34f),
      )
      AdminTextField(
          "Latitud inicio",
          routeForm.startLatitude,
          { onRouteFormChange(routeForm.copy(startLatitude = it)) },
          modifier = Modifier.weight(0.33f),
      )
      AdminTextField(
          "Longitud inicio",
          routeForm.startLongitude,
          { onRouteFormChange(routeForm.copy(startLongitude = it)) },
          modifier = Modifier.weight(0.33f),
      )
    }

    Text(
        "Punto final",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminDropdownField(
          label = "Provincia final",
          value = routeForm.endProvince,
          placeholder = "Selecciona una provincia",
          options = provinces.map { it.name },
          onSelect = { provinceName ->
            onRouteFormChange(
                routeForm.copy(
                    endProvince = provinceName,
                    endCanton = "",
                    endDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Cantón final",
          value = routeForm.endCanton,
          placeholder = "Selecciona un cantón",
          options = endCantons.map { it.name },
          enabled = endCantons.isNotEmpty(),
          onSelect = { cantonName ->
            onRouteFormChange(
                routeForm.copy(
                    endCanton = cantonName,
                    endDistrict = "",
                )
            )
          },
          modifier = Modifier.weight(1f),
      )
      AdminDropdownField(
          label = "Distrito final",
          value = routeForm.endDistrict,
          placeholder = "Selecciona un distrito",
          options = endDistricts,
          enabled = endDistricts.isNotEmpty(),
          onSelect = { districtName ->
            onRouteFormChange(routeForm.copy(endDistrict = districtName))
          },
          modifier = Modifier.weight(1f),
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      AdminTextField(
          "Latitud final",
          routeForm.endLatitude,
          { onRouteFormChange(routeForm.copy(endLatitude = it)) },
          modifier = Modifier.weight(1f),
      )
      AdminTextField(
          "Longitud final",
          routeForm.endLongitude,
          { onRouteFormChange(routeForm.copy(endLongitude = it)) },
          modifier = Modifier.weight(1f),
      )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = routeForm.acceptsCardPayment,
            onCheckedChange = { onRouteFormChange(routeForm.copy(acceptsCardPayment = it)) },
        )
        Text("Acepta tarjeta")
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = routeForm.isActive,
            onCheckedChange = { onRouteFormChange(routeForm.copy(isActive = it)) },
        )
        Text("Ruta activa")
      }
    }
    Button(
        onClick = onSaveRoute,
        enabled = !isSaving && routeForm.code.isNotBlank() && routeForm.name.isNotBlank(),
        shape = RoundedCornerShape(16.dp),
    ) {
      Icon(Icons.Default.Save, contentDescription = null)
      Spacer(Modifier.width(8.dp))
      Text(if (isSaving) "Guardando..." else "Guardar ruta")
    }
  }
}
