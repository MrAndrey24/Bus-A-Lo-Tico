package com.example.busalotico.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import busalotico.composeapp.generated.resources.Res
import busalotico.composeapp.generated.resources.logo
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.chip.SelectableFilterChip
import com.example.shared.domain.entities.UserRole
import com.example.shared.presentation.auth.AuthUiState
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (name: String, email: String, password: String, role: UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.PASSENGER) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFF4F4F6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Bus A Lo Tico",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isRegisterMode) "CREAR CUENTA" else "INICIAR SESION",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF8B8E95)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E6EB))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = if (isRegisterMode) {
                            "Crea una cuenta."
                        } else {
                            "Ingresar"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6F7279)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (isRegisterMode) {
                        AuthTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nombre"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }
                    )

                    if (isRegisterMode) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ROL",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF7B7D84)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            UserRole.entries.forEach { role ->
                                SelectableFilterChip(
                                    text = role.toDisplayName(),
                                    selected = selectedRole == role,
                                    onClick = { selectedRole = role },
                                    showChevron = false
                                )
                            }
                        }
                    }

                    uiState.errorMessage?.let { errorMessage ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFF9A2F2F),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PillActionButton(
                        text = if (uiState.isLoading) "Procesando..." else if (isRegisterMode) "Crear cuenta" else "Iniciar sesion",
                        onClick = {
                            if (isRegisterMode) {
                                onSignUp(
                                    name.ifBlank { email.substringBefore("@") },
                                    email,
                                    password,
                                    selectedRole
                                )
                            } else {
                                onSignIn(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color(0xFF1B1D23),
                        contentColor = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PillActionButton(
                        text = if (isRegisterMode) "Ya tengo cuenta" else "Crear cuenta",
                        onClick = { isRegisterMode = !isRegisterMode },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color(0xFFE7E7EC),
                        contentColor = Color(0xFF66636F)
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text(label) },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword && onPasswordVisibilityToggle != null) {
            {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = Color(0xFF6F7279),
                    modifier = Modifier
                        .clickable { onPasswordVisibilityToggle() }
                        .padding(end = 4.dp)
                )
            }
        } else {
            null
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFFD2D4DB),
            unfocusedBorderColor = Color(0xFFE5E6EB),
            focusedLabelColor = Color(0xFF6F7279),
            unfocusedLabelColor = Color(0xFF8B8E95),
            focusedTextColor = Color(0xFF2A2C33),
            unfocusedTextColor = Color(0xFF2A2C33)
        )
    )
}

private fun UserRole.toDisplayName(): String {
    return when (this) {
        UserRole.PASSENGER -> "Pasajero"
        UserRole.DRIVER -> "Conductor"
        UserRole.ADMIN -> "Administrador"
    }
}
