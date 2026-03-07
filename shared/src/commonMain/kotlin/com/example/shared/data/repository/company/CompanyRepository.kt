package com.example.shared.data.repository.company

import com.example.shared.data.remote.company.ICompanyRemoteDataSource
import com.example.shared.domain.entities.Company

class CompanyRepository(
    private val remoteDataSource: ICompanyRemoteDataSource
) : ICompanyRepository {

    override suspend fun getCompany(companyId: String): Company? {
        return remoteDataSource.getCompany(companyId)
    }
}
