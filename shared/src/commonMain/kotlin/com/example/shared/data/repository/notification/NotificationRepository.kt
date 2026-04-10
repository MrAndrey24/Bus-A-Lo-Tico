package com.example.shared.data.repository.notification

import com.example.shared.data.remote.datasource.notification.INotificationRemoteDataSource
import com.example.shared.data.remote.dto.UserIncidentNotificationDto
import com.example.shared.domain.entities.UserIncidentNotification
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
