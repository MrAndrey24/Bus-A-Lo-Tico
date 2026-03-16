package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.user.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteViewModel(
    private val routeRepository: IRouteRepository,
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()
    private var activeUserId: String? = null

    init {
        loadRoutes()
        onUserChanged(authRepository.getCurrentUserId())
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

    fun onUserChanged(userId: String?) {
        if (activeUserId == userId) return

        activeUserId = userId
        _uiState.value = _uiState.value.copy(
            favoriteRouteIds = emptySet(),
            errorMessage = null
        )

        loadFavoriteRouteIds(userId)
    }

    private fun loadFavoriteRouteIds(userId: String?) {
        val currentUserId = userId ?: return

        viewModelScope.launch {
            runCatching {
                userRepository.getFavoriteRouteIds(currentUserId)
            }.onSuccess { favoriteRouteIds ->
                _uiState.value = _uiState.value.copy(
                    favoriteRouteIds = favoriteRouteIds,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "No se pudieron cargar las favoritas"
                )
            }
        }
    }

    fun toggleFavorite(routeId: String, isFavorite: Boolean) {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            runCatching {
                userRepository.setRouteFavorite(currentUserId, routeId, isFavorite)
            }.onSuccess {
                val updatedFavoriteRouteIds = if (isFavorite) {
                    _uiState.value.favoriteRouteIds + routeId
                } else {
                    _uiState.value.favoriteRouteIds - routeId
                }

                _uiState.value = _uiState.value.copy(
                    favoriteRouteIds = updatedFavoriteRouteIds,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message ?: "No se pudo actualizar la favorita"
                )
            }
        }
    }
}
