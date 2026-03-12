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
    val fontFamily = FontFamily(
        Font(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
        Font(Res.font.plus_jakarta_sans_medium, FontWeight.Medium),
        Font(Res.font.plus_jakarta_sans_bold, FontWeight.Bold)
    )
    return androidx.compose.material3.Typography(
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontSize = 14.sp
        ),
    )
}