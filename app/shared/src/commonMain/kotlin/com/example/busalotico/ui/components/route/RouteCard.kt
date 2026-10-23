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
package com.example.busalotico.ui.components.route

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busalotico.app.shared.generated.resources.logo
import com.example.busalotico.ui.model.RouteListItemUi
import org.jetbrains.compose.resources.painterResource

@Composable
fun RouteSelectionCard(
    route: RouteListItemUi,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShowAdministrativeDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
  Card(
      modifier = modifier.fillMaxWidth().clickable(enabled = route.isActive, onClick = onClick),
      shape = RoundedCornerShape(16.dp),
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface,
          ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
            text = route.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onShowAdministrativeDetail, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp),
            )
          }
          IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Favorite",
                tint =
                    if (route.isFavorite) Color(0xFFFFB300)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                modifier = Modifier.size(22.dp),
            )
          }
        }
      }

      val startLocation =
          listOfNotNull(
                  route.province?.takeIf { it.isNotBlank() },
                  route.canton?.takeIf { it.isNotBlank() },
                  route.district?.takeIf { it.isNotBlank() },
              )
              .joinToString(", ")

      val endLocation =
          listOfNotNull(
                  route.endProvince?.takeIf { it.isNotBlank() },
                  route.endCanton?.takeIf { it.isNotBlank() },
                  route.endDistrict?.takeIf { it.isNotBlank() },
              )
              .joinToString(", ")

      // Route Path (Timeline style)
      RouteTimeline(
          start = startLocation.ifBlank { "Origen" },
          end = endLocation.ifBlank { "Destino" },
      )

      // Bottom Info Bar
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (route.isLive || route.isDelayed) {
            LiveBadge(
                text = route.statusText,
                isDelayed = route.isDelayed,
            )
            Spacer(modifier = Modifier.width(8.dp))
          }

          Icon(
              painter =
                  painterResource(busalotico.app.shared.generated.resources.Res.drawable.logo),
              contentDescription = null,
              modifier = Modifier.size(14.dp).alpha(0.0f),
          )

          Text(
              text = route.etaText,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Medium,
          )
        }

        RouteCodeBadge(
            code = route.fareCrc?.let { "Tarifa: ₡$it" } ?: route.code,
            isActive = route.isActive,
        )
      }
    }
  }
}

@Composable
private fun RouteTimeline(start: String, end: String, modifier: Modifier = Modifier) {
  Row(
      modifier = modifier.fillMaxWidth().height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(20.dp).fillMaxHeight(),
    ) {
      Box(
          modifier =
              Modifier.padding(top = 4.dp)
                  .size(8.dp)
                  .background(MaterialTheme.colorScheme.primary, CircleShape),
      )
      Box(
          modifier =
              Modifier.width(2.dp).weight(1f).background(MaterialTheme.colorScheme.outlineVariant),
      )
      Box(
          modifier =
              Modifier.padding(bottom = 6.dp)
                  .size(8.dp)
                  .background(Color.Transparent, CircleShape)
                  .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
          text = start.toDisplayLocationName(),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 16.sp,
      )
      Text(
          text = end.toDisplayLocationName(),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 18.sp,
      )
    }
  }
}

@Composable
fun RouteCodeBadge(code: String, isActive: Boolean, modifier: Modifier = Modifier) {
  val containerColor =
      if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
  val contentColor =
      if (isActive) MaterialTheme.colorScheme.onPrimary
      else MaterialTheme.colorScheme.onSurfaceVariant

  Surface(color = containerColor, shape = RoundedCornerShape(8.dp), modifier = modifier) {
    Text(
        text = code,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = contentColor,
    )
  }
}

@Composable
fun LiveBadge(
    text: String = "EN VIVO",
    isDelayed: Boolean = false,
    modifier: Modifier = Modifier,
) {
  val containerColor =
      if (isDelayed) Color(0xFFFFF0DD) else MaterialTheme.colorScheme.tertiaryContainer
  val dotColor = if (isDelayed) Color(0xFFD67C2E) else MaterialTheme.colorScheme.tertiary
  val contentColor =
      if (isDelayed) Color(0xFF8A5A18) else MaterialTheme.colorScheme.onTertiaryContainer

  Surface(color = containerColor, shape = RoundedCornerShape(6.dp), modifier = modifier) {
    Row(
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(modifier = Modifier.size(6.dp).background(dotColor, CircleShape))
      Spacer(modifier = Modifier.width(4.dp))
      Text(
          text = text,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = contentColor,
      )
    }
  }
}

private fun String.toDisplayLocationName(): String {
  return trim().lowercase().split(Regex("\\s+")).joinToString(" ") { word ->
    word.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() }
  }
}
