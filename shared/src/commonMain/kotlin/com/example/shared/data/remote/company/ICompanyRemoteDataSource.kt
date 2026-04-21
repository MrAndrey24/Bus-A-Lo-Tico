/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.company

import com.example.shared.domain.entities.Company

interface ICompanyRemoteDataSource {
  suspend fun getCompany(companyId: String): Company?

  suspend fun getCompanies(): List<Company>
}
