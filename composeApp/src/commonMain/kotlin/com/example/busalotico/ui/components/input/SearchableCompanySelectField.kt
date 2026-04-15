package com.example.busalotico.ui.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shared.domain.entities.Company

@Composable
fun SearchableCompanySelectField(
    value: String,
    placeholder: String,
    enabled: Boolean,
    companies: List<Company>,
    onSelect: (Company) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Empresa",
    showCompanyId: Boolean = true,
    containerColor: Color = Color.White,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
) {
  var showDialog by remember { mutableStateOf(false) }
  var query by remember { mutableStateOf("") }

  val filteredCompanies = remember(query, companies) {
    val normalizedQuery = query.trim()
    if (normalizedQuery.isBlank()) {
      companies.sortedBy { it.name.lowercase() }
    } else {
      companies
          .filter { company ->
            company.name.contains(normalizedQuery, ignoreCase = true) ||
                company.id.contains(normalizedQuery, ignoreCase = true)
          }
          .sortedBy { it.name.lowercase() }
    }
  }

  Surface(
      modifier = modifier.fillMaxWidth().clickable(enabled = enabled) { showDialog = true },
      shape = RoundedCornerShape(16.dp),
      color = containerColor,
      border = border,
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.ifBlank { placeholder },
            style = MaterialTheme.typography.bodyMedium,
            color =
                if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
        )
      }
      Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }

  if (showDialog) {
    AlertDialog(
        onDismissRequest = { showDialog = false },
        containerColor = Color(0xFFF3F5F7),
        title = {
          Text(
              text = "Seleccionar empresa",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
          )
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Buscar por nombre") },
                shape = RoundedCornerShape(14.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.75f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                    ),
            )

            if (filteredCompanies.isEmpty()) {
              Text(
                  text = "No se encontraron empresas.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            } else {
              LazyColumn(
                  modifier = Modifier.fillMaxWidth().height(280.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                items(filteredCompanies, key = { it.id }) { company ->
                  Surface(
                      modifier =
                          Modifier.fillMaxWidth().clickable {
                            onSelect(company)
                            query = ""
                            showDialog = false
                          },
                      shape = RoundedCornerShape(14.dp),
                      color = Color(0xFFE9EDF2),
                  ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                      Text(
                          text = company.name,
                          style = MaterialTheme.typography.bodyMedium,
                          color = MaterialTheme.colorScheme.onSurface,
                          fontWeight = FontWeight.SemiBold,
                      )
                      if (showCompanyId) {
                        Text(
                            text = company.id,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showDialog = false }) {
            Text("Cerrar")
          }
        },
    )
  }
}
