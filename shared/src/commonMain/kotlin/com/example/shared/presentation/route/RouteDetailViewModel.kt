package com.example.shared.presentation.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.route.IRouteRepository
import com.example.shared.domain.entities.Coordinate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteDetailViewModel(
    private val routeRepository: IRouteRepository
) : ViewModel() {

    private val _selectedRouteId = MutableStateFlow("SJ-204")
    val selectedRouteId: StateFlow<String> = _selectedRouteId.asStateFlow()

    private val _polylinePoints = MutableStateFlow<List<Coordinate>>(emptyList())
    val polylinePoints: StateFlow<List<Coordinate>> = _polylinePoints.asStateFlow()

    fun updateSelectedRoute(id: String) {
        _selectedRouteId.value = id
    }

    // ESTA ES LA NUEVA QUE OCUPAMOS PARA LA LÍNEA
    fun loadMapboxLine(stops: List<Coordinate>) {
        viewModelScope.launch {
            val line = routeRepository.getRouteGeometry(stops)
            _polylinePoints.value = line
        }
    }
}