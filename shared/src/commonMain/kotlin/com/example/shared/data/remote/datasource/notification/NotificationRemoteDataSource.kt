package com.example.shared.data.remote.datasource.notification

import com.example.shared.data.remote.dto.UserIncidentNotificationDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRemoteDataSource : INotificationRemoteDataSource {

  override fun listenToUserNotifications(userId: String): Flow<List<UserIncidentNotificationDto>> {
    val query = Firebase.firestore.collection("users").document(userId).collection("notifications")

    return query.snapshots().map { querySnapshot ->
      querySnapshot.documents
          .mapNotNull { doc ->
            try {
              doc.data<UserIncidentNotificationDto>()
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
    Firebase.firestore
        .collection("users")
        .document(userId)
        .collection("notifications")
        .document(notificationId)
        .update("readAt" to Clock.System.now().toEpochMilliseconds())
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
