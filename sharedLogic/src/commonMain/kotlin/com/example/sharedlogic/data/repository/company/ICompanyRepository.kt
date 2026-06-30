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
package com.example.sharedlogic.data.repository.company

import com.example.sharedlogic.domain.entities.Company

interface ICompanyRepository {
  suspend fun getCompany(companyId: String): Company?

  suspend fun getCompanies(): List<Company>
}
