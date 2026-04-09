package com.example.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserIncidentNotificationDto(
    val id: String = "",
    val userId: String = "",
    val tripExecutionId: String = "",
    val incidentType: String = "",
    val detail: String = "",
    val sanitizedDetail: String = "",
    val routeName: String = "",
    val dispatchedAt: Long = 0L,
    val readAt: Long? = null,
    val createdAt: Long = 0L,
) {
  fun toEntity() =
      com.example.shared.domain.entities.UserIncidentNotification(
          id = id,
          userId = userId,
          tripExecutionId = tripExecutionId,
          incidentType = incidentType,
          detail = detail,
          sanitizedDetail = sanitizedDetail,
          routeName = routeName,
          dispatchedAt = dispatchedAt,
          readAt = readAt,
          createdAt = createdAt,
      )

  companion object {
    fun fromEntity(entity: com.example.shared.domain.entities.UserIncidentNotification) =
        UserIncidentNotificationDto(
            id = entity.id,
            userId = entity.userId,
            tripExecutionId = entity.tripExecutionId,
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
