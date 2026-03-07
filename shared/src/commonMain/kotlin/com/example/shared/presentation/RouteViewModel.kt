package com.example.shared.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.entities.Route
import com.example.shared.domain.usecases.GetRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteViewModel(
    private val getRouteUseCase: GetRouteUseCase
) : ViewModel() {

    private val _route = MutableStateFlow<Route?>(null)
    val route: StateFlow<Route?> = _route.asStateFlow()

    fun loadRoute(routeId: String) {
        viewModelScope.launch {
            _route.value = getRouteUseCase(routeId)
        }
    }
}
