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
package com.example.shared.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.driver.IDriverRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.domain.entities.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: IAuthRepository,
    private val driverRepository: IDriverRepository,
    private val userRepository: IUserRepository,
) : ViewModel() {

  private val _uiState = MutableStateFlow(AuthUiState())
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

  init {
    loadCurrentSession()
  }

  fun loadCurrentSession() {
    val currentUserId = authRepository.getCurrentUserId()
    if (currentUserId == null) {
      _uiState.value = AuthUiState()
      return
    }

    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
      val profile = userRepository.getUserProfile(currentUserId)
      _uiState.value =
          _uiState.value.copy(isLoading = false, currentUser = profile, errorMessage = null)
    }
  }

  fun signIn(email: String, password: String) {
    viewModelScope.launch {
      runCatching {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val userId = authRepository.signIn(email, password)
            userRepository.getUserProfile(userId)
                ?: userRepository.upsertUserProfile(
                    userId = userId,
                    email = email,
                    name = email.substringBefore("@"),
                    role = UserRole.PASSENGER,
                    companyId = null,
                )
          }
          .onSuccess { profile ->
            _uiState.value = AuthUiState(isLoading = false, currentUser = profile)
          }
          .onFailure { error ->
            _uiState.value =
                AuthUiState(
                    isLoading = false,
                    errorMessage = error.toSpanishAuthMessage("No se pudo iniciar sesion"),
                )
          }
    }
  }

  fun signUp(
      name: String,
      email: String,
      password: String,
      role: UserRole,
      companyId: String? = null,
  ) {
    viewModelScope.launch {
      runCatching {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val normalizedCompanyId = companyId?.trim().takeUnless { it.isNullOrBlank() }
            val userId = authRepository.signUp(email, password)
            val profile =
                userRepository.upsertUserProfile(
                    userId = userId,
                    email = email,
                    name = name,
                    role = role,
                    companyId = normalizedCompanyId,
                )
            if (role == UserRole.DRIVER) {
              driverRepository.upsertDriver(
                  driverId = userId,
                  name = name,
                  companyId = null,
              )
            }
            profile
          }
          .onSuccess { profile ->
            _uiState.value = AuthUiState(isLoading = false, currentUser = profile)
          }
          .onFailure { error ->
            _uiState.value =
                AuthUiState(
                    isLoading = false,
                    errorMessage = error.toSpanishAuthMessage("No se pudo registrar el usuario"),
                )
          }
    }
  }

  fun updateCurrentUserCompany(companyId: String) {
    val currentUser = _uiState.value.currentUser ?: return
    viewModelScope.launch {
      runCatching {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val updatedProfile =
                userRepository.upsertUserProfile(
                    userId = currentUser.id,
                    email = currentUser.email,
                    name = currentUser.name,
                    role = currentUser.role,
                    companyId = companyId.trim(),
                )
            if (currentUser.role == UserRole.DRIVER) {
              driverRepository.upsertDriver(
                  driverId = currentUser.id,
                  name = currentUser.name,
                  companyId = companyId.trim(),
              )
            }
            updatedProfile
          }
          .onSuccess { profile ->
            _uiState.value =
                _uiState.value.copy(isLoading = false, currentUser = profile, errorMessage = null)
          }
          .onFailure { error ->
            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudo actualizar la empresa.",
                )
          }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      authRepository.signOut()
      _uiState.value = AuthUiState()
    }
  }
}

private fun Throwable.toSpanishAuthMessage(defaultMessage: String): String {
  val rawMessage = message.orEmpty()
  val normalizedMessage = rawMessage.lowercase()

  return when {
    "auth/invalid-email" in normalizedMessage || "invalid-email" in normalizedMessage ->
        "El correo tiene un formato invalido."
    "auth/user-disabled" in normalizedMessage || "user-disabled" in normalizedMessage ->
        "Esta cuenta ha sido deshabilitada."
    "auth/user-not-found" in normalizedMessage || "user-not-found" in normalizedMessage ->
        "No existe una cuenta con ese correo."
    "auth/wrong-password" in normalizedMessage || "wrong-password" in normalizedMessage ->
        "La contrasena es incorrecta."
    "auth/email-already-in-use" in normalizedMessage ||
        "email-already-in-use" in normalizedMessage -> "Ese correo ya esta registrado."
    "auth/operation-not-allowed" in normalizedMessage ||
        "operation-not-allowed" in normalizedMessage ->
        "El acceso con correo y contrasena no esta habilitado en Firebase."
    "auth/weak-password" in normalizedMessage || "weak-password" in normalizedMessage ->
        "La contrasena es demasiado debil."
    "configuration_not_found" in normalizedMessage ->
        "La autenticacion no esta configurada en Firebase. Activa Email/Password en Authentication."
    rawMessage.isBlank() -> defaultMessage
    else -> defaultMessage
  }
}
