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
import com.example.sharedlogic.domain.entities.Company
import com.example.sharedlogic.domain.usecases.company.GetCompanyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyViewModel(private val getCompanyUseCase: GetCompanyUseCase) : ViewModel() {

  private val _company = MutableStateFlow<Company?>(null)
  val company: StateFlow<Company?> = _company.asStateFlow()

  fun loadCompany(companyId: String) {
    viewModelScope.launch { _company.value = getCompanyUseCase(companyId) }
  }
}
