/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.company

import com.example.shared.domain.entities.Company

data class CompanyListUiState(
    val isLoading: Boolean = false,
    val companies: List<Company> = emptyList(),
    val errorMessage: String? = null,
)
