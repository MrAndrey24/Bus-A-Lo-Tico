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
package com.example.sharedlogic.domain.entities

data class UserIncidentNotification(
    val id: String = "",
    val userId: String = "",
    val tripExecutionId: String = "",
    val routeId: String = "",
    val incidentType: String = "",
    val detail: String = "",
    val sanitizedDetail: String = "",
    val routeName: String = "",
    val dispatchedAt: Long = 0L,
    val readAt: Long? = null,
    val createdAt: Long = 0L,
)
