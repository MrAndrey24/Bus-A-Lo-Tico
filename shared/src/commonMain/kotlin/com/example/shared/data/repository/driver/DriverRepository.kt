package com.example.shared.data.repository.driver

import com.example.shared.data.remote.driver.IDriverRemoteDataSource
import com.example.shared.domain.entities.Driver

class DriverRepository(
    private val remoteDataSource: IDriverRemoteDataSource
) : IDriverRepository {

    override suspend fun getDriver(driverId: String): Driver? {
        return remoteDataSource.getDriver(driverId)
    }
}
