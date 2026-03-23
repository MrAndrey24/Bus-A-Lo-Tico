package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.domain.usecases.route.SearchRoutesByLocationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteViewModel(
    private val routeRepository: IRouteRepository,
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
    private val searchRoutesByLocationUseCase: SearchRoutesByLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()
    private var activeUserId: String? = null
    private var searchJob: Job? = null

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

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
        
        searchJob?.cancel()
        
        if (newQuery.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isSearching = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.value = _uiState.value.copy(isSearching = true)
            
            try {
                val results = searchRoutesByLocationUseCase(newQuery)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    errorMessage = e.message ?: "Error searching routes"
                )
            }
        }
    }

    fun onClearSearch() {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
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
