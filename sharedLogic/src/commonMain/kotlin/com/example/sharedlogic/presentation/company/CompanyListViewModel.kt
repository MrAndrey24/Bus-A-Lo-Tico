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
package com.example.sharedlogic.presentation.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedlogic.data.repository.company.ICompanyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyListViewModel(
    private val companyRepository: ICompanyRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyListUiState(isLoading = true))
  val uiState: StateFlow<CompanyListUiState> = _uiState.asStateFlow()

  init {
    loadCompanies()
  }

  fun loadCompanies() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      runCatching { companyRepository.getCompanies() }
          .onSuccess { companies ->
            _uiState.value =
                CompanyListUiState(
                    isLoading = false,
                    companies = companies,
                )
          }
          .onFailure { error ->
            _uiState.value =
                CompanyListUiState(
                    isLoading = false,
                    companies = emptyList(),
                    errorMessage = error.message ?: "No se pudieron cargar las empresas.",
                )
          }
    }
  }
}
