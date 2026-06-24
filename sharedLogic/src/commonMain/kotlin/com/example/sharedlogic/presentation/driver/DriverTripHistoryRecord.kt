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
package com.example.sharedlogic.presentation.driver

data class DriverTripHistoryRecord(
    val tripExecutionId: String,
    val routeName: String,
    val departureTime: String,
    val startedAtLabel: String,
    val endedAtLabel: String,
    val statusLabel: String = "COMPLETADO",
    val incidentTypeLabel: String? = null,
    val incidentDetail: String? = null,
)
