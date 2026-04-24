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
package com.example.shared.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.notification.INotificationRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notifications: List<UserIncidentNotification> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class NotificationViewModel(
    private val notificationRepository: INotificationRepository,
    private val routeRepository: IRouteRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(NotificationUiState())
  val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

  fun loadNotifications(userId: String) {
    if (userId.isBlank()) {
      _uiState.value = NotificationUiState(errorMessage = "Usuario no autenticado.")
      return
    }

    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

    viewModelScope.launch {
      notificationRepository.observeUserNotifications(userId).collectLatest { notifications ->
        val routeNameCache = mutableMapOf<String, String>()
        val displayNotifications =
            notifications.map { notification ->
              val resolvedRouteName =
                  notification.routeName.takeIf { it.isNotBlank() }
                      ?: routeNameCache.getOrPut(notification.routeId) {
                        routeRepository.getRoute(notification.routeId)?.name.orEmpty()
                      }

              notification.copy(routeName = resolvedRouteName).toDisplayNotification()
            }
        _uiState.value =
            NotificationUiState(
                notifications = displayNotifications,
                isLoading = false,
                errorMessage = null,
            )
      }
    }
  }

  fun markNotificationAsRead(notificationId: String, userId: String) {
    viewModelScope.launch {
      notificationRepository.markNotificationAsRead(notificationId, userId).onFailure {
        _uiState.value =
            _uiState.value.copy(errorMessage = "No se pudo marcar la notificación como leída.")
      }
    }
  }

  fun deleteNotification(notificationId: String, userId: String) {
    viewModelScope.launch {
      notificationRepository.deleteNotification(notificationId, userId).onFailure {
        _uiState.value = _uiState.value.copy(errorMessage = "No se pudo eliminar la notificación.")
      }
    }
  }
}
