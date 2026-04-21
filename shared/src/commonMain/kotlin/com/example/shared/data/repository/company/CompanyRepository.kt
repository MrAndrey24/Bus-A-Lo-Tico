/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.company

import com.example.shared.data.remote.company.ICompanyRemoteDataSource
import com.example.shared.domain.entities.Company

class CompanyRepository(private val remoteDataSource: ICompanyRemoteDataSource) :
    ICompanyRepository {

  override suspend fun getCompany(companyId: String): Company? {
    return remoteDataSource.getCompany(companyId)
  }

  override suspend fun getCompanies(): List<Company> {
    return remoteDataSource.getCompanies()
  }
}
