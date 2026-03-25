package com.example.busalotico.ui.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.busalotico.ui.components.header.ScreenHeader

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    userRole: String,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
          Column(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 28.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Surface(
                modifier = Modifier.size(92.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = userName.ifBlank { "Usuario" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
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

            if (userRole.isNotBlank()) {
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                  text = userRole,
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
              )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileInfoRow(label = "Nombre", value = userName.ifBlank { "-" })
            Spacer(modifier = Modifier.height(12.dp))
            ProfileInfoRow(label = "Correo", value = userEmail.ifBlank { "-" })
            Spacer(modifier = Modifier.height(12.dp))
            ProfileInfoRow(label = "Rol", value = userRole.ifBlank { "-" })
          }
        }
      }
    }
  }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 11.dp)) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(modifier = Modifier.height(3.dp))
      Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
  }
}
