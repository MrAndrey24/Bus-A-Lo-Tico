/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.screen.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busalotico.composeapp.generated.resources.Res
import busalotico.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun EnableLocationScreen(onAllowClick: () -> Unit, onDenyClick: () -> Unit) {
  var visible by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) { visible = true }

  Box(
      modifier =
          Modifier.fillMaxSize()
              .background(
                  Brush.verticalGradient(
                      colors =
                          listOf(
                              MaterialTheme.colorScheme.background,
                              MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                          )
                  )
              )
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.weight(0.5f))

      AnimatedVisibility(
          visible = visible,
          enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
      ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).clip(RoundedCornerShape(24.dp))) {
          Image(
              painter = painterResource(Res.drawable.logo),
              contentDescription = "Ubicación ilustración",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Fit,
          )
        }
      }

      Spacer(modifier = Modifier.height(48.dp))

      Text(
          text = "¡Encuentra tu bus!",
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary,
          textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
          text =
              "Para mostrarte las paradas más cercanas y el tiempo real de los buses, necesitamos acceso a tu ubicación.",
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 24.sp,
          modifier = Modifier.padding(horizontal = 8.dp),
      )

      Spacer(modifier = Modifier.weight(1f))

      Button(
          onClick = onAllowClick,
          modifier = Modifier.fillMaxWidth().height(60.dp),
          shape = RoundedCornerShape(16.dp),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
          colors =
              ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary,
              ),
      ) {
        Text(text = "Permitir siempre", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(12.dp))

      TextButton(
          onClick = onDenyClick,
          modifier = Modifier.fillMaxWidth().height(56.dp),
          shape = RoundedCornerShape(16.dp),
      ) {
        Text(
            text = "Más tarde",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
        )
      }
    }
  }
}
