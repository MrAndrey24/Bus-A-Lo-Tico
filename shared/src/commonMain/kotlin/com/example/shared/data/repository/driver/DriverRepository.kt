package com.example.shared.data.repository.driver

import com.example.shared.data.remote.driver.IDriverRemoteDataSource
import com.example.shared.domain.entities.Driver

class DriverRepository(
    private val remoteDataSource: IDriverRemoteDataSource
) : IDriverRepository {

    override suspend fun getDriver(driverId: String): Driver? {
        return remoteDataSource.getDriver(driverId)
    }

    override suspend fun getDriversByCompany(companyId: String): List<Driver> {
        return remoteDataSource.getDriversByCompany(companyId)
    }

    override suspend fun upsertDriver(driverId: String, name: String, companyId: String?): Driver {
        return remoteDataSource.upsertDriver(driverId, name, companyId)
    }
}
