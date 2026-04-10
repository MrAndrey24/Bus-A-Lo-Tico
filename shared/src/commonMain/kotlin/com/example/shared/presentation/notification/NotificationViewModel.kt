package com.example.shared.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.notification.INotificationRepository
import com.example.shared.domain.entities.UserIncidentNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationUiState(
    val notifications: List<UserIncidentNotification> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class NotificationViewModel(
    private val notificationRepository: INotificationRepository,
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
      notificationRepository.observeUserNotifications(userId).collect { notifications ->
        _uiState.value =
            NotificationUiState(
                notifications = notifications.map(UserIncidentNotification::toDisplayNotification),
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
