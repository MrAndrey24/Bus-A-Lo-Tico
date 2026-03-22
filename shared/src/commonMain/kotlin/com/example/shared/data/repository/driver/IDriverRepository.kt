package com.example.shared.data.repository.driver

import com.example.shared.domain.entities.Driver

interface IDriverRepository {
    suspend fun getDriver(driverId: String): Driver?
}
