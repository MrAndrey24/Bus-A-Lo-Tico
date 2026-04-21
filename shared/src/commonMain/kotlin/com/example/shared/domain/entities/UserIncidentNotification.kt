/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.entities

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
