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
package com.example.busalotico.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import busalotico.app.shared.generated.resources.Res
import busalotico.app.shared.generated.resources.plus_jakarta_sans_bold
import busalotico.app.shared.generated.resources.plus_jakarta_sans_medium
import busalotico.app.shared.generated.resources.plus_jakarta_sans_regular
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
  val fontFamily =
      FontFamily(
          Font(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
          Font(Res.font.plus_jakarta_sans_medium, FontWeight.Medium),
          Font(Res.font.plus_jakarta_sans_bold, FontWeight.Bold),
      )
  return androidx.compose.material3.Typography(
      titleLarge =
          TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp),
      bodyLarge = TextStyle(fontFamily = fontFamily, fontSize = 16.sp),
      bodyMedium = TextStyle(fontFamily = fontFamily, fontSize = 14.sp),
  )
}
