package com.example.shared.domain.repository

import com.example.shared.domain.entities.Company

interface ICompanyRepository {
    suspend fun getCompany(companyId: String): Company?
}
