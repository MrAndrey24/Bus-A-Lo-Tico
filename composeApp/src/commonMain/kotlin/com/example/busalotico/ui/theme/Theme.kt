package com.example.busalotico.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val AppColorScheme = lightColorScheme(

    primary = DeepNavy,
    onPrimary = White,

    background = White,
    onBackground = NearBlack,

    surface = LightGray,
    onSurface = NearBlack,

    secondary = MediumGray,
    outline = PaleGray,

    tertiary = SuccessGreen
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = appTypography(),
        content = content
    )
}