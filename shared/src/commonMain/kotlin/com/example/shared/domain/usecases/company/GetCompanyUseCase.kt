package com.example.shared.domain.usecases.company

import com.example.shared.data.repository.company.ICompanyRepository
import com.example.shared.domain.entities.Company

class GetCompanyUseCase(
    private val repository: ICompanyRepository
) {
    suspend operator fun invoke(companyId: String): Company? = repository.getCompany(companyId)
}