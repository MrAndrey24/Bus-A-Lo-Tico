package com.example.shared.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.repository.auth.IAuthRepository
import com.example.shared.data.repository.user.IUserRepository
import com.example.shared.domain.entities.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
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
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                currentUser = profile,
                errorMessage = null
            )
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
                        role = UserRole.PASSENGER
                    )
            }.onSuccess { profile ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    currentUser = profile
                )
            }.onFailure { error ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.toSpanishAuthMessage("No se pudo iniciar sesion")
                )
            }
        }
    }

    fun signUp(name: String, email: String, password: String, role: UserRole) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val userId = authRepository.signUp(email, password)
                userRepository.upsertUserProfile(
                    userId = userId,
                    email = email,
                    name = name,
                    role = role
                )
            }.onSuccess { profile ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    currentUser = profile
                )
            }.onFailure { error ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.toSpanishAuthMessage("No se pudo registrar el usuario")
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
        "configuration_not_found" in normalizedMessage ->
            "La autenticacion no esta configurada en Firebase. Activa Email/Password en Authentication."
        "supplied auth credential is incorrect" in normalizedMessage ->
            "El correo o la contrasena son incorrectos."
        "wrong-password" in normalizedMessage ->
            "La contrasena es incorrecta."
        "user-not-found" in normalizedMessage ->
            "No existe una cuenta con ese correo."
        "email address is badly formatted" in normalizedMessage ->
            "El correo tiene un formato invalido."
        "invalid-email" in normalizedMessage ->
            "El correo tiene un formato invalido."
        "email-already-in-use" in normalizedMessage ->
            "Ese correo ya esta registrado."
        "password should be at least" in normalizedMessage ->
            "La contrasena debe tener al menos 6 caracteres."
        "weak-password" in normalizedMessage ->
            "La contrasena es demasiado debil."
        "network error" in normalizedMessage ->
            "No se pudo conectar. Revisa tu conexion a internet."
        rawMessage.isBlank() ->
            defaultMessage
        else ->
            defaultMessage
    }
}
