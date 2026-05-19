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
package com.example.sharedlogic.data.remote.datasource.notification

import com.example.sharedlogic.data.remote.dto.UserIncidentNotificationDto
import kotlinx.coroutines.flow.Flow

interface INotificationRemoteDataSource {
  fun listenToUserNotifications(userId: String): Flow<List<UserIncidentNotificationDto>>

  suspend fun markNotificationAsRead(notificationId: String, userId: String): Result<Unit>

  suspend fun deleteNotification(notificationId: String, userId: String): Result<Unit>

  suspend fun addNotification(notification: UserIncidentNotificationDto): Result<String>
}
