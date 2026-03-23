package com.example.shared.data.remote.driver

import com.example.shared.domain.entities.Driver

interface IDriverRemoteDataSource {
    suspend fun getDriver(driverId: String): Driver?
    suspend fun upsertDriver(driverId: String, name: String, companyId: String? = null): Driver
}
