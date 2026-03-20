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
import androidx.compose.ui.graphics.Color
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
  Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF4F4F6)) {
    Column(modifier = Modifier.fillMaxSize()) {
      ScreenHeader(
          title = "Perfil",
          subtitle = "Informacion de la cuenta",
          trailingContent = {
            IconButton(onClick = onSignOut) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.Logout,
                  contentDescription = "Cerrar sesion",
                  tint = Color(0xFF555861),
              )
            }
          },
      )

      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 24.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE3E4E8)),
        ) {
          Column(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 28.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Surface(
                modifier = Modifier.size(92.dp),
                shape = CircleShape,
                color = Color(0xFFF0F1F4),
                border = BorderStroke(1.dp, Color(0xFFE3E4E8)),
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint = Color(0xFF5A5D66),
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = userName.ifBlank { "Usuario" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF17181C),
            )

            if (userEmail.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                  text = userEmail,
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color(0xFF7B7D84),
              )
            }

            if (userRole.isNotBlank()) {
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                  text = userRole,
                  style = MaterialTheme.typography.labelMedium,
                  color = Color(0xFF8B8E95),
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
      color = Color(0xFFF7F7F9),
      border = BorderStroke(1.dp, Color(0xFFE7E8EC)),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 11.dp)) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF8B8E95))
      Spacer(modifier = Modifier.height(3.dp))
      Text(text = value, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF1E1F24))
    }
  }
}
