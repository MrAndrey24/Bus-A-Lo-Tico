/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
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
