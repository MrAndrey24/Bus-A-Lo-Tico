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
package com.example.sharedlogic.data.repository.notification

import com.example.sharedlogic.data.remote.datasource.notification.INotificationRemoteDataSource
import com.example.sharedlogic.data.remote.dto.UserIncidentNotificationDto
import com.example.sharedlogic.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepository(
    private val remoteDataSource: INotificationRemoteDataSource,
) : INotificationRepository {

  override fun observeUserNotifications(userId: String): Flow<List<UserIncidentNotification>> =
      remoteDataSource.listenToUserNotifications(userId).map { dtos -> dtos.map { it.toEntity() } }

  override suspend fun markNotificationAsRead(
      notificationId: String,
      userId: String,
  ): Result<Unit> = remoteDataSource.markNotificationAsRead(notificationId, userId)

  override suspend fun deleteNotification(
      notificationId: String,
      userId: String,
  ): Result<Unit> = remoteDataSource.deleteNotification(notificationId, userId)

  override suspend fun addNotification(notification: UserIncidentNotification): Result<String> =
      remoteDataSource.addNotification(UserIncidentNotificationDto.fromEntity(notification))
}
