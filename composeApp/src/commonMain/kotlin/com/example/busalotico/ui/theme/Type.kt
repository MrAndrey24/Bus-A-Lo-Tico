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

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import busalotico.composeapp.generated.resources.Res
import busalotico.composeapp.generated.resources.plus_jakarta_sans_bold
import busalotico.composeapp.generated.resources.plus_jakarta_sans_medium
import busalotico.composeapp.generated.resources.plus_jakarta_sans_regular
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
