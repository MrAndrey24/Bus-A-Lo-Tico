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
