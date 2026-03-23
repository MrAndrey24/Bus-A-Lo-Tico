package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.route.IRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DriverRouteViewModel(
    private val routeRepository: IRouteRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(DriverRouteUiState())
  val uiState: StateFlow<DriverRouteUiState> = _uiState.asStateFlow()

  fun loadRouteForDriver(driverId: String) {
    if (driverId.isBlank()) {
      _uiState.value = DriverRouteUiState(errorMessage = "No se encontró el conductor autenticado.")
      return
    }

    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

    viewModelScope.launch {
      runCatching { routeRepository.getRouteByDriverId(driverId) }
          .onSuccess { route ->
            _uiState.value =
                if (route != null) {
                  DriverRouteUiState(route = route)
                } else {
                  DriverRouteUiState(
                      errorMessage = "No hay una ruta asignada a este conductor.",
                  )
                }
          }
          .onFailure {
            _uiState.value =
                DriverRouteUiState(
                    errorMessage = "No se pudo cargar la ruta del conductor.",
                )
          }
    }
  }
}
