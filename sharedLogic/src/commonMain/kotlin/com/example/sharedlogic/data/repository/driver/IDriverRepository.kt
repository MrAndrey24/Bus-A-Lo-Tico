/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.repository.driver

import com.example.sharedlogic.domain.entities.Driver

interface IDriverRepository {
  suspend fun getDriver(driverId: String): Driver?

  suspend fun getDriversByCompany(companyId: String): List<Driver>

  suspend fun upsertDriver(driverId: String, name: String, companyId: String? = null): Driver

  suspend fun deleteDriver(driverId: String)
}
