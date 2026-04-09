package com.example.shared.data.remote.datasource.notification

import com.example.shared.data.remote.dto.UserIncidentNotificationDto
import kotlinx.coroutines.flow.Flow

interface INotificationRemoteDataSource {
  fun listenToUserNotifications(userId: String): Flow<List<UserIncidentNotificationDto>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun deleteNotification(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotificationDto): Result<String>
}
