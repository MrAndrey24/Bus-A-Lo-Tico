/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.driver

import com.example.shared.domain.entities.Driver

interface IDriverRepository {
  suspend fun getDriver(driverId: String): Driver?

  suspend fun getDriversByCompany(companyId: String): List<Driver>

  suspend fun upsertDriver(driverId: String, name: String, companyId: String? = null): Driver

  suspend fun deleteDriver(driverId: String)
}
