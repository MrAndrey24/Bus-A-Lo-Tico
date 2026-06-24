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
package com.example.sharedlogic.data.remote.datasource.notification

import com.example.sharedlogic.data.remote.dto.UserIncidentNotificationDto
import kotlinx.coroutines.flow.Flow

interface INotificationRemoteDataSource {
  fun listenToUserNotifications(userId: String): Flow<List<UserIncidentNotificationDto>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun deleteNotification(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotificationDto): Result<String>
}
