package com.example.shared.data.repository.notification

import com.example.shared.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
  fun observeUserNotifications(userId: String): Flow<List<UserIncidentNotification>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotification): Result<String>
}
