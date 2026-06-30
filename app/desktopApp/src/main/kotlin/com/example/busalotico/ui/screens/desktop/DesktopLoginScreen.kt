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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DesktopLoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onSignIn: (String, String) -> Unit,
) {
  var email by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  Box(
      modifier = Modifier.fillMaxSize().background(Color(0xFFF1F3F6)).padding(32.dp),
      contentAlignment = Alignment.Center,
  ) {
    Column(
        modifier = Modifier.width(520.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      ElevatedCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(30.dp),
          colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
          elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
      ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
          Surface(
              modifier = Modifier.width(74.dp).height(74.dp),
              shape = RoundedCornerShape(22.dp),
              color = Color(0xFF122B4A),
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                  imageVector = Icons.Default.DirectionsBus,
                  contentDescription = null,
                  tint = Color.White,
              )
            }
          }
          Text(
              text = "Bus A Lo Tico",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
          )
          Text(
              text = "Viaja informado",
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.bodyLarge,
          )
          Text(
              text = "Ingresa con una cuenta administrativa para acceder al panel empresarial.",
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              style = MaterialTheme.typography.bodyMedium,
          )
          TextField(
              value = email,
              onValueChange = { email = it },
              modifier = Modifier.fillMaxWidth(),
              label = { Text("Correo") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              colors =
                  TextFieldDefaults.colors(
                      focusedContainerColor = Color(0xFFF1F3F6),
                      unfocusedContainerColor = Color(0xFFF1F3F6),
                      disabledContainerColor = Color(0xFFF1F3F6),
                      focusedIndicatorColor = Color.Transparent,
                      unfocusedIndicatorColor = Color.Transparent,
                      disabledIndicatorColor = Color.Transparent,
                  ),
          )
          TextField(
              value = password,
              onValueChange = { password = it },
              modifier = Modifier.fillMaxWidth(),
              label = { Text("Contraseña") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              visualTransformation =
                  if (passwordVisible) VisualTransformation.None
                  else PasswordVisualTransformation(),
              trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                  Icon(
                      imageVector =
                          if (passwordVisible) Icons.Outlined.Visibility
                          else Icons.Outlined.VisibilityOff,
                      contentDescription =
                          if (passwordVisible) "Contraseña visible" else "Contraseña oculta",
                  )
                }
              },
              colors =
                  TextFieldDefaults.colors(
                      focusedContainerColor = Color(0xFFF1F3F6),
                      unfocusedContainerColor = Color(0xFFF1F3F6),
                      disabledContainerColor = Color(0xFFF1F3F6),
                      focusedIndicatorColor = Color.Transparent,
                      unfocusedIndicatorColor = Color.Transparent,
                      disabledIndicatorColor = Color.Transparent,
                  ),
          )
          errorMessage?.let { message ->
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFDECEC)) {
              Text(
                  text = message,
                  modifier = Modifier.padding(14.dp),
                  color = Color(0xFFB3261E),
              )
            }
          }
          Button(
              onClick = { onSignIn(email.trim(), password) },
              enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
              modifier = Modifier.fillMaxWidth().height(54.dp),
              shape = RoundedCornerShape(16.dp),
          ) {
            if (isLoading) {
              CircularProgressIndicator(
                  modifier = Modifier.width(20.dp).height(20.dp),
                  color = Color.White,
                  strokeWidth = 2.dp,
              )
            } else {
              Text("Entrar al panel")
            }
          }
        }
      }
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
              modifier =
                  Modifier.width(18.dp)
                      .height(2.dp)
                      .background(Color(0xFF2C63D6), RoundedCornerShape(999.dp))
          )
          Box(
              modifier =
                  Modifier.width(30.dp)
                      .height(2.dp)
                      .background(Color(0xFFE25555), RoundedCornerShape(999.dp))
          )
          Box(
              modifier =
                  Modifier.width(18.dp)
                      .height(2.dp)
                      .background(Color(0xFF2C63D6), RoundedCornerShape(999.dp))
          )
        }
        Text(
            text = "PURA VIDA • COSTA RICA",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.Medium,
        )
      }
    }
  }
}
