/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico.data

import androidx.compose.ui.graphics.vector.ImageVector
import dev.icerock.moko.permissions.Permission

data class PermissionRequest(
    val permission: Permission,
    val title: String,
    val description: String,
    val icon: ImageVector,
)
