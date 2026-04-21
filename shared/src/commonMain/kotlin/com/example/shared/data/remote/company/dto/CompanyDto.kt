/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.company.dto

import com.example.shared.domain.entities.Company
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(val name: String = "") {
  fun toDomain(documentId: String): Company = Company(id = documentId, name = name)
}
