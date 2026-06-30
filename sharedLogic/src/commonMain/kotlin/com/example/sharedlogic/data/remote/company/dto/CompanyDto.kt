/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.remote.company.dto

import com.example.sharedlogic.domain.entities.Company
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(val name: String = "") {
  fun toDomain(documentId: String): Company = Company(id = documentId, name = name)
}
