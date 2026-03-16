package com.example.shared.presentation.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.entities.Company
import com.example.shared.domain.usecases.company.GetCompanyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyViewModel(
    private val getCompanyUseCase: GetCompanyUseCase
) : ViewModel() {

    private val _company = MutableStateFlow<Company?>(null)
    val company: StateFlow<Company?> = _company.asStateFlow()

    fun loadCompany(companyId: String) {
        viewModelScope.launch {
            _company.value = getCompanyUseCase(companyId)
        }
    }
}