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
package com.example.sharedlogic.presentation.auth

import com.example.sharedlogic.domain.entities.UserProfile

data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: UserProfile? = null,
    val errorMessage: String? = null,
) {
  val isAuthenticated: Boolean
    get() = currentUser != null
}
