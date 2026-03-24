package com.example.busalotico.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
  var visible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) { visible = true }

  Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    AnimatedVisibility(visible = visible, enter = fadeIn() + scaleIn(initialScale = 0.94f)) {
      Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center).offset(y = (-72).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
          Icon(
              imageVector = Icons.Filled.DirectionsBus,
              contentDescription = "Bus",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.width(78.dp).height(78.dp),
          )

          Spacer(modifier = Modifier.height(28.dp))

          Text(
              text =
                  buildAnnotatedString {
                    append("Bus ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("A Lo Tico")
                    pop()
                  },
              style =
                  MaterialTheme.typography.titleLarge.copy(
                      color = MaterialTheme.colorScheme.onBackground,
                      fontSize = 28.sp,
                  ),
              textAlign = TextAlign.Center,
              maxLines = 1,
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
              text = "TRANSITANDO COSTA RICA ",
              style =
                  MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.secondary,
                      fontWeight = FontWeight.Medium,
                      letterSpacing = 4.sp,
                  ),
              textAlign = TextAlign.Center,
          )
        }
      }
    }

    Column(
        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      FooterFlagLine()

      Spacer(modifier = Modifier.height(16.dp))

      Text(
          text = "PURA VIDA \u2022 2026",
          style =
              MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Medium,
                  letterSpacing = 3.sp,
              ),
          textAlign = TextAlign.Center,
      )
    }
  }
}

@Composable
private fun FooterFlagLine() {
  Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
    Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF2C63BA)))
    Box(modifier = Modifier.width(34.dp).height(2.dp).background(Color(0xFFD85858)))
    Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF2C63BA)))
  }
}
