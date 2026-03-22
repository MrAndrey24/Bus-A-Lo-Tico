package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.route.IRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteAdministrativeDetailViewModel(
    private val routeRepository: IRouteRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(RouteAdministrativeDetailUiState())
  val uiState: StateFlow<RouteAdministrativeDetailUiState> = _uiState.asStateFlow()

  fun loadRoute(routeId: String) {
    if (routeId.isBlank()) {
      _uiState.value =
          RouteAdministrativeDetailUiState(
              errorMessage = "No se encontró la ruta seleccionada.",
          )
      return
    }

    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

    viewModelScope.launch {
      runCatching { routeRepository.getRoute(routeId) }
          .onSuccess { route ->
            _uiState.value =
                if (route != null) {
                  RouteAdministrativeDetailUiState(route = route)
                } else {
                  RouteAdministrativeDetailUiState(
                      errorMessage = "No se encontró la ficha administrativa de esta ruta.",
                  )
                }
          }
          .onFailure {
            _uiState.value =
                RouteAdministrativeDetailUiState(
                    errorMessage = "No se pudo cargar la ficha administrativa.",
                )
          }
    }
  }
}
