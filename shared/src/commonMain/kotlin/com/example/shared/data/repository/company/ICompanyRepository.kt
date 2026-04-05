package com.example.shared.data.repository.company

import com.example.shared.domain.entities.Company

interface ICompanyRepository {
    suspend fun getCompany(companyId: String): Company?
    suspend fun getCompanies(): List<Company>
}
