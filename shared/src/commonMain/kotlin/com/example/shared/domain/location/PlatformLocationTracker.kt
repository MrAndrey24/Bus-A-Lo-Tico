package com.example.shared.domain.location

import com.example.shared.domain.entities.Coordinate
import kotlinx.coroutines.flow.Flow

interface PlatformLocationTracker {
    fun getLocationUpdates(): Flow<Coordinate>
    suspend fun startTracking()
    suspend fun stopTracking()
}
