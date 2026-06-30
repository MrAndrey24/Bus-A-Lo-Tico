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
package com.example.sharedlogic.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserIncidentNotificationDto(
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
) {
  fun toEntity() =
      com.example.sharedlogic.domain.entities.UserIncidentNotification(
          id = id,
          userId = userId,
          tripExecutionId = tripExecutionId,
          routeId = routeId,
          incidentType = incidentType,
          detail = detail,
          sanitizedDetail = sanitizedDetail,
          routeName = routeName,
          dispatchedAt = dispatchedAt,
          readAt = readAt,
          createdAt = createdAt,
      )

  companion object {
    fun fromEntity(entity: com.example.sharedlogic.domain.entities.UserIncidentNotification) =
        UserIncidentNotificationDto(
            id = entity.id,
            userId = entity.userId,
            tripExecutionId = entity.tripExecutionId,
            routeId = entity.routeId,
            incidentType = entity.incidentType,
            detail = entity.detail,
            sanitizedDetail = entity.sanitizedDetail,
            routeName = entity.routeName,
            dispatchedAt = entity.dispatchedAt,
            readAt = entity.readAt,
            createdAt = entity.createdAt,
        )
  }
}
