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
package com.example.sharedlogic.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.notification.INotificationRepository
import com.example.sharedlogic.data.repository.route.IRouteRepository
import com.example.sharedlogic.domain.entities.UserIncidentNotification
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
        val displayNotifications = notifications.map { notification ->
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
