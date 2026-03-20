package com.example.busalotico.ui.components.mapview.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.busalotico.library.R

@Composable
fun StopIcon() {
    Image(
        painter = painterResource(id = R.drawable.stops),
        contentDescription = "Parada",
        modifier = Modifier.size(36.dp)
    )
}