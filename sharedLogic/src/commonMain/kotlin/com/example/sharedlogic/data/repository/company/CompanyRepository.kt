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
package com.example.sharedlogic.data.repository.company

import com.example.sharedlogic.data.remote.company.ICompanyRemoteDataSource
import com.example.sharedlogic.domain.entities.Company

class CompanyRepository(private val remoteDataSource: ICompanyRemoteDataSource) :
    ICompanyRepository {

  override suspend fun getCompany(companyId: String): Company? {
    return remoteDataSource.getCompany(companyId)
  }

  override suspend fun getCompanies(): List<Company> {
    return remoteDataSource.getCompanies()
  }
}
