package com.example.shared.presentation.company

import com.example.shared.domain.entities.Company

data class CompanyListUiState(
    val isLoading: Boolean = false,
    val companies: List<Company> = emptyList(),
    val errorMessage: String? = null,
)
