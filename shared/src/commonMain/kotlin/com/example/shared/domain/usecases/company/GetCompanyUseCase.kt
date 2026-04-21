/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.domain.usecases.company

import com.example.shared.data.repository.company.ICompanyRepository
import com.example.shared.domain.entities.Company

class GetCompanyUseCase(private val repository: ICompanyRepository) {
  suspend operator fun invoke(companyId: String): Company? = repository.getCompany(companyId)
}
