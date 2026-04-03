package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.RouteStop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteStopMaintenanceViewModel(
    private val routeRepository: IRouteRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(RouteStopMaintenanceUiState())
  val uiState: StateFlow<RouteStopMaintenanceUiState> = _uiState.asStateFlow()

  fun loadRouteStops(routeId: String) {
    if (routeId.isBlank()) {
      _uiState.value = RouteStopMaintenanceUiState(errorMessage = "No se encontró la ruta.")
      return
    }

    _uiState.value =
        _uiState.value.copy(
            routeId = routeId,
            isLoading = true,
            errorMessage = null,
            successMessage = null,
        )

    viewModelScope.launch {
      runCatching { routeRepository.getRoute(routeId) }
          .onSuccess { route ->
            if (route == null) {
              _uiState.value =
                  _uiState.value.copy(
                      isLoading = false,
                      errorMessage = "No se pudieron cargar las paradas de esta ruta.",
                  )
            } else {
              _uiState.value =
                  _uiState.value.copy(
                      isLoading = false,
                      stops = route.stops.sortedBy { it.order },
                  )
            }
          }
          .onFailure {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudieron cargar las paradas de esta ruta.",
                )
          }
    }
  }

  fun onNameChanged(value: String) {
    _uiState.value = _uiState.value.copy(name = value, errorMessage = null, successMessage = null)
  }

  fun onLatitudeChanged(value: String) {
    _uiState.value = _uiState.value.copy(latitude = value, errorMessage = null, successMessage = null)
  }

  fun onLongitudeChanged(value: String) {
    _uiState.value = _uiState.value.copy(longitude = value, errorMessage = null, successMessage = null)
  }

  fun onOrderChanged(value: String) {
    _uiState.value = _uiState.value.copy(order = value, errorMessage = null, successMessage = null)
  }

  fun selectStop(stop: RouteStop) {
    _uiState.value =
        _uiState.value.copy(
            selectedStopId = stop.id,
            name = stop.name,
            latitude = stop.latitude.toString(),
            longitude = stop.longitude.toString(),
            order = stop.order.toString(),
            errorMessage = null,
            successMessage = null,
        )
  }

  fun clearForm() {
    _uiState.value =
        _uiState.value.copy(
            selectedStopId = null,
            name = "",
            latitude = "",
            longitude = "",
            order = "",
            errorMessage = null,
            successMessage = null,
        )
  }

  fun saveStop() {
    val state = _uiState.value
    val routeId = state.routeId
    val name = state.name.trim()
    val latitude = state.latitude.toDoubleOrNull()
    val longitude = state.longitude.toDoubleOrNull()
    val order = state.order.toIntOrNull()

    if (routeId.isBlank()) {
      _uiState.value = state.copy(errorMessage = "No se encontró la ruta.")
      return
    }
    if (name.isBlank() || latitude == null || longitude == null || order == null) {
      _uiState.value =
          state.copy(errorMessage = "Completa nombre, latitud, longitud y orden.")
      return
    }

    _uiState.value = state.copy(isSaving = true, errorMessage = null, successMessage = null)

    viewModelScope.launch {
      runCatching {
            routeRepository.upsertRouteStop(
                routeId = routeId,
                stop =
                    RouteStop(
                        id = state.selectedStopId.orEmpty(),
                        name = name,
                        latitude = latitude,
                        longitude = longitude,
                        order = order,
                    ),
            )
          }
          .onSuccess { savedStop ->
            refreshStops(
                routeId = routeId,
                successMessage =
                    if (state.selectedStopId.isNullOrBlank()) {
                      "Parada guardada correctamente."
                    } else {
                      "Parada actualizada correctamente."
                    },
                selectedStopId = savedStop.id,
            )
          }
          .onFailure {
            _uiState.value =
                _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "No se pudo guardar la parada.",
                )
          }
    }
  }

  fun deleteSelectedStop() {
    val state = _uiState.value
    val routeId = state.routeId
    val stopId = state.selectedStopId

    if (routeId.isBlank() || stopId.isNullOrBlank()) {
      _uiState.value = state.copy(errorMessage = "Selecciona una parada para eliminar.")
      return
    }

    _uiState.value = state.copy(isDeleting = true, errorMessage = null, successMessage = null)

    viewModelScope.launch {
      runCatching { routeRepository.deleteRouteStop(routeId, stopId) }
          .onSuccess {
            clearForm()
            refreshStops(
                routeId = routeId,
                successMessage = "Parada eliminada correctamente.",
                selectedStopId = null,
            )
          }
          .onFailure {
            _uiState.value =
                _uiState.value.copy(
                    isDeleting = false,
                    errorMessage = "No se pudo eliminar la parada.",
                )
          }
    }
  }

  fun moveStopUp(stopId: String) {
    moveStop(stopId = stopId, direction = -1)
  }

  fun moveStopDown(stopId: String) {
    moveStop(stopId = stopId, direction = 1)
  }

  private fun moveStop(stopId: String, direction: Int) {
    val state = _uiState.value
    val routeId = state.routeId
    val stops = state.stops.sortedBy { it.order }
    val currentIndex = stops.indexOfFirst { it.id == stopId }

    if (routeId.isBlank() || currentIndex == -1) {
      _uiState.value = state.copy(errorMessage = "No se pudo reordenar la parada.")
      return
    }

    val targetIndex = currentIndex + direction
    if (targetIndex !in stops.indices) {
      return
    }

    val currentStop = stops[currentIndex]
    val targetStop = stops[targetIndex]

    _uiState.value = state.copy(isSaving = true, errorMessage = null, successMessage = null)

    viewModelScope.launch {
      runCatching {
            routeRepository.upsertRouteStop(
                routeId = routeId,
                stop = currentStop.copy(order = targetStop.order),
            )
            routeRepository.upsertRouteStop(
                routeId = routeId,
                stop = targetStop.copy(order = currentStop.order),
            )
          }
          .onSuccess {
            refreshStops(
                routeId = routeId,
                successMessage = "Orden de paradas actualizado.",
                selectedStopId = currentStop.id,
            )
          }
          .onFailure {
            _uiState.value =
                _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "No se pudo reordenar la parada.",
                )
          }
    }
  }

  private suspend fun refreshStops(
      routeId: String,
      successMessage: String,
      selectedStopId: String?,
  ) {
    val route = routeRepository.getRoute(routeId)
    val refreshedStops = route?.stops?.sortedBy { it.order }.orEmpty()
    val selectedStop = refreshedStops.firstOrNull { it.id == selectedStopId }
    _uiState.value =
        _uiState.value.copy(
            isLoading = false,
            isSaving = false,
            isDeleting = false,
            stops = refreshedStops,
            selectedStopId = selectedStopId,
            successMessage = successMessage,
            errorMessage = null,
            name = selectedStop?.name ?: "",
            latitude = selectedStop?.latitude?.toString() ?: "",
            longitude = selectedStop?.longitude?.toString() ?: "",
            order = selectedStop?.order?.toString() ?: "",
        )
  }
}
