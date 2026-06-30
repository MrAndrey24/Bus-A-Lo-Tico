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
package com.example.sharedlogic.domain.entities

import com.example.sharedlogic.domain.entities.enums.TripExecutionStatus

data class TripExecution(
    val id: String = "",
    val scheduleId: String,
    val routeId: String,
    val driverId: String,
    val startedAt: Long,
    val endedAt: Long? = null,
    val durationSeconds: Int? = null,
    val status: TripExecutionStatus = TripExecutionStatus.IN_PROGRESS,
    val hadIncidents: Boolean = false,
    val transferredToDriverId: String? = null,
    val transferredAt: Long? = null,
)
