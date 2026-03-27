package com.example.busalotico.ui.components.searchBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shared.domain.entities.Route

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean,
    searchResults: List<Route>,
    onRouteSelected: (Route) -> Unit,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
) {
  val showResults = isSearching || searchResults.isNotEmpty()

  Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
      Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp),
          )

          BasicTextField(
              value = query,
              onValueChange = onQueryChange,
              modifier = Modifier.weight(1f).padding(vertical = 12.dp),
              singleLine = true,
              textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
              decorationBox = { innerTextField ->
                Box {
                  if (query.isEmpty()) {
                    Text(
                        text = placeholder ?: "¿Adónde vas?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                  }
                  innerTextField()
                }
              },
          )

          when {
            isSearching -> {
              CircularProgressIndicator(
                  modifier = Modifier.size(18.dp),
                  strokeWidth = 2.dp,
              )
            }

            query.isNotEmpty() -> {
              Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Limpiar",
                  tint = Color.Gray,
                  modifier = Modifier.size(18.dp).clickable { onQueryChange("") },
              )
            }
          }
        }

        if (showResults) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))

          when {
            isSearching -> {
              Box(
                  modifier = Modifier.fillMaxWidth().padding(16.dp),
                  contentAlignment = Alignment.Center,
              ) {
                Text(
                    text = "Buscando rutas...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            searchResults.isEmpty() && query.isNotBlank() -> {
              Box(
                  modifier = Modifier.fillMaxWidth().padding(16.dp),
                  contentAlignment = Alignment.Center,
              ) {
                Text(
                    text = "No se encontraron rutas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            else -> {
              LazyColumn(
                  modifier = Modifier.fillMaxWidth().heightIn(max = 240.dp),
                  verticalArrangement = Arrangement.spacedBy(2.dp),
              ) {
                items(searchResults, key = { it.id }) { route ->
                  SearchResultItem(
                      route = route,
                      onClick = { onRouteSelected(route) },
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
private fun SearchResultItem(route: Route, onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
        imageVector = Icons.Default.DirectionsBus,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(20.dp),
    )

    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      Text(
          text = route.name.trim().uppercase(),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Medium,
      )
      Text(
          text = route.code,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
