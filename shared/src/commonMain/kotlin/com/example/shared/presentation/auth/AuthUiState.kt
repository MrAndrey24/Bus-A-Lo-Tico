/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.presentation.auth

import com.example.shared.domain.entities.UserProfile

data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: UserProfile? = null,
    val errorMessage: String? = null,
) {
  val isAuthenticated: Boolean
    get() = currentUser != null
}
