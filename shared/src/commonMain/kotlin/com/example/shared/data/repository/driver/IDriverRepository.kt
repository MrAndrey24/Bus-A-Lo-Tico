package com.example.shared.data.repository.driver

import com.example.shared.domain.entities.Driver

interface IDriverRepository {
    suspend fun getDriver(driverId: String): Driver?
    suspend fun getDriversByCompany(companyId: String): List<Driver>
    suspend fun upsertDriver(driverId: String, name: String, companyId: String? = null): Driver
    suspend fun deleteDriver(driverId: String)
}
