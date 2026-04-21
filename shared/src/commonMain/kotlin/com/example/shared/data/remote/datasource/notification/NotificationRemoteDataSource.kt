/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.datasource.notification

import com.example.shared.data.remote.dto.UserIncidentNotificationDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRemoteDataSource : INotificationRemoteDataSource {

  override fun listenToUserNotifications(userId: String): Flow<List<UserIncidentNotificationDto>> {
    val normalizedUserId = userId.trim()
    val query =
        Firebase.firestore
            .collection("users")
            .document(normalizedUserId)
            .collection("notifications")

    return query.snapshots().map { querySnapshot ->
      querySnapshot.documents
          .mapNotNull { doc ->
            try {
              // Always use the Firestore document id to support legacy documents without `id`
              // field.
              doc.data<UserIncidentNotificationDto>().copy(id = doc.id)
            } catch (e: Exception) {
              null
            }
          }
          .sortedByDescending { it.dispatchedAt }
    }
  }

  override suspend fun markNotificationAsRead(
      notificationId: String,
      userId: String,
  ): Result<Unit> = runCatching {
    val normalizedUserId = userId.trim()
    val normalizedNotificationId = notificationId.trim()
    require(normalizedUserId.isNotBlank()) { "User id is required." }
    require(normalizedNotificationId.isNotBlank()) { "Notification id is required." }

    Firebase.firestore
        .collection("users")
        .document(normalizedUserId)
        .collection("notifications")
        .document(normalizedNotificationId)
        .update("readAt" to Clock.System.now().toEpochMilliseconds())
  }

  override suspend fun deleteNotification(
      notificationId: String,
      userId: String,
  ): Result<Unit> = runCatching {
    val normalizedUserId = userId.trim()
    val normalizedNotificationId = notificationId.trim()
    require(normalizedUserId.isNotBlank()) { "User id is required." }
    require(normalizedNotificationId.isNotBlank()) { "Notification id is required." }

    Firebase.firestore
        .collection("users")
        .document(normalizedUserId)
        .collection("notifications")
        .document(normalizedNotificationId)
        .delete()
  }

  override suspend fun addNotification(notification: UserIncidentNotificationDto): Result<String> =
      runCatching {
        val docId = "doc_${Clock.System.now().toEpochMilliseconds()}"
        Firebase.firestore
            .collection("users")
            .document(notification.userId)
            .collection("notifications")
            .document(docId)
            .set(notification.copy(id = docId))
        docId
      }
}
