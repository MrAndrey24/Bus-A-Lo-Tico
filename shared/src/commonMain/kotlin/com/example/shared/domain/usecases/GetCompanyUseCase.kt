package com.example.shared.domain.usecases

import com.example.shared.domain.entities.Company
import com.example.shared.domain.repository.ICompanyRepository

class GetCompanyUseCase(
    private val repository: ICompanyRepository
) {
    suspend operator fun invoke(companyId: String): Company? = repository.getCompany(companyId)
}
