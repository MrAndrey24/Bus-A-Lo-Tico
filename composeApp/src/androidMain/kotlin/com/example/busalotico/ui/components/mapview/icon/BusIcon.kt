/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.ui.components.mapview.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.busalotico.library.R

@Composable
fun BusIcon() {
  Image(
      painter = painterResource(id = R.drawable.bus),
      contentDescription = "Bus",
      modifier = Modifier.size(36.dp),
  )
}
