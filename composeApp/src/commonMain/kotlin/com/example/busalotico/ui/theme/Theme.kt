package com.example.busalotico.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val AppColorScheme =
    lightColorScheme(
        primary = DeepNavy,
        onPrimary = White,
        primaryContainer = LightNavy,
        onPrimaryContainer = DeepNavy,
        background = White,
        onBackground = NearBlack,
        surface = LightGray,
        onSurface = NearBlack,
        surfaceVariant = PaleGray,
        onSurfaceVariant = MediumGray,
        secondary = MediumGray,
        secondaryContainer = PaleGray,
        onSecondaryContainer = NearBlack,
        outline = PaleGray,
        tertiary = SuccessGreen,
    )

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = AppColorScheme, typography = appTypography(), content = content)
}
