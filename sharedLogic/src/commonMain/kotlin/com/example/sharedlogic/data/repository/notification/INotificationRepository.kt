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
package com.example.sharedlogic.data.repository.notification

import com.example.sharedlogic.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
  fun observeUserNotifications(userId: String): Flow<List<UserIncidentNotification>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun deleteNotification(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotification): Result<String>
}
