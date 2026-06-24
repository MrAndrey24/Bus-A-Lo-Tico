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
package com.example.sharedlogic.domain.usecases.company

import com.example.sharedlogic.data.repository.company.ICompanyRepository
import com.example.sharedlogic.domain.entities.Company

class GetCompanyUseCase(private val repository: ICompanyRepository) {
  suspend operator fun invoke(companyId: String): Company? = repository.getCompany(companyId)
}
