/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.shared.presentation.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.company.ICompanyRepository
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
