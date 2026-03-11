package com.example.shared.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RouteDetailViewModel : ViewModel() {
    private val _selectedRouteId = MutableStateFlow("SJ-204")

    val selectedRouteId: StateFlow<String> = _selectedRouteId.asStateFlow()

    fun updateSelectedRoute(id: String) {
        _selectedRouteId.value = id
    }
}