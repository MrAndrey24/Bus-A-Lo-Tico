/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
