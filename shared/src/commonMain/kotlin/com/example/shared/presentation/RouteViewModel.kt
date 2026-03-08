package com.example.shared.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.route.IRouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteViewModel( private val routeRepository: IRouteRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                routes = emptyList(),
                errorMessage = null
            )
            try {
                val routes = routeRepository.getRoutes()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    routes = routes,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    routes = emptyList(),
                    errorMessage = e.message ?: "Error loading routes"
                )
            }
        }
    }
}
