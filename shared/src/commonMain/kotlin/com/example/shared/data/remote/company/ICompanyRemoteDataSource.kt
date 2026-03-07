package com.example.shared.data.remote.company

import com.example.shared.domain.entities.Company

interface ICompanyRemoteDataSource {
    suspend fun getCompany(companyId: String): Company?
}
