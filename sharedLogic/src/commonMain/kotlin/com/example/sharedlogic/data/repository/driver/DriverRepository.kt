/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.data.repository.driver

import com.example.sharedlogic.data.remote.driver.IDriverRemoteDataSource
import com.example.sharedlogic.domain.entities.Driver

class DriverRepository(private val remoteDataSource: IDriverRemoteDataSource) : IDriverRepository {

  override suspend fun getDriver(driverId: String): Driver? {
    return remoteDataSource.getDriver(driverId)
  }

  override suspend fun getDriversByCompany(companyId: String): List<Driver> {
    return remoteDataSource.getDriversByCompany(companyId)
  }

  override suspend fun upsertDriver(driverId: String, name: String, companyId: String?): Driver {
    return remoteDataSource.upsertDriver(driverId, name, companyId)
  }

  override suspend fun deleteDriver(driverId: String) {
    remoteDataSource.deleteDriver(driverId)
  }
}
