/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
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
        onTertiary = White,
        tertiaryContainer = SuccessGreenContainer,
        onTertiaryContainer = OnSuccessGreenContainer,
        error = ErrorRed,
        onError = White,
        errorContainer = ErrorRedContainer,
        onErrorContainer = OnErrorRedContainer,
    )

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = AppColorScheme, typography = appTypography(), content = content)
}
