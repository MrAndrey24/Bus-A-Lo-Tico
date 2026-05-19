/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
