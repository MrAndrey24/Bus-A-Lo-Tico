package com.example.shared.data.local

import com.example.shared.domain.entities.Coordinate
import com.example.shared.domain.location.PlatformLocationTracker
import kotlinx.coroutines.flow.Flow

fun interface ILocationLocalDataSource {
    fun getLocationUpdates(tracker: PlatformLocationTracker) : Flow<Coordinate>
}
