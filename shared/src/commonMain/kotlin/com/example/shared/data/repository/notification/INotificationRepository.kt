/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.notification

import com.example.shared.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
  fun observeUserNotifications(userId: String): Flow<List<UserIncidentNotification>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun deleteNotification(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotification): Result<String>
}
