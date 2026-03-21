package com.example.busalotico.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.busalotico.ui.components.button.PillActionButton
import com.example.busalotico.ui.components.chip.SelectableFilterChip
import com.example.shared.domain.entities.UserRole
import com.example.shared.presentation.auth.AuthUiState

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (name: String, email: String, password: String, role: UserRole) -> Unit,
    modifier: Modifier = Modifier,
) {
  var isRegisterMode by remember { mutableStateOf(false) }
  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var selectedRole by remember { mutableStateOf(UserRole.PASSENGER) }

  Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF6F6F3)) {
    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
      Column(
          modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(top = 72.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Box(
            modifier = Modifier.size(64.dp).background(Color(0xFFEEEEEA), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
          Icon(
              imageVector = Icons.Filled.DirectionsBus,
              contentDescription = "Bus",
              tint = Color(0xFF1B1D23),
              modifier = Modifier.size(28.dp),
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = if (isRegisterMode) "Crea tu cuenta" else "Bus A Lo Tico",
            style =
                MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF1B1D23),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text =
                if (isRegisterMode) {
                  "Regístrate."
                } else {
                  "Viaja informado"
                },
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF7D7D7A),
                    fontWeight = FontWeight.Medium,
                ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFF0F0EC)),
            shadowElevation = 4.dp,
        ) {
          Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            if (isRegisterMode) {
              FieldLabel("NOMBRE")
              AuthTextField(value = name, onValueChange = { name = it }, placeholder = "Tu nombre")
              Spacer(modifier = Modifier.height(16.dp))
            }

            FieldLabel("CORREO ELECTRÓNICO")
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "nombre@ejemplo.com",
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("CONTRASEÑA")
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            )

            if (isRegisterMode) {
              Spacer(modifier = Modifier.height(16.dp))

              FieldLabel("ROL")
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UserRole.entries.forEach { role ->
                  SelectableFilterChip(
                      text = role.toDisplayName(),
                      selected = selectedRole == role,
                      onClick = { selectedRole = role },
                      showChevron = false,
                  )
                }
              }
            }

            uiState.errorMessage?.let { errorMessage ->
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                  text = errorMessage,
                  color = Color(0xFF9A2F2F),
                  style = MaterialTheme.typography.bodySmall,
              )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PillActionButton(
                text =
                    if (uiState.isLoading) "Procesando..."
                    else if (isRegisterMode) "Crear cuenta" else "Iniciar Sesión",
                onClick = {
                  if (isRegisterMode) {
                    onSignUp(
                        name.ifBlank { email.substringBefore("@") },
                        email,
                        password,
                        selectedRole,
                    )
                  } else {
                    onSignIn(email, password)
                  }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFF1B1D23),
                contentColor = Color.White,
            )

            if (!isRegisterMode) {
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                  text = "¿Olvidaste tu contraseña?",
                  modifier = Modifier.fillMaxWidth(),
                  textAlign = TextAlign.Center,
                  style =
                      MaterialTheme.typography.bodySmall.copy(
                          color = Color(0xFFA4A4A0),
                          fontWeight = FontWeight.Medium,
                      ),
              )

              Spacer(modifier = Modifier.height(18.dp))

              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp),
              ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF0F0EC))
                Text(
                    text = "O CONTINUA CON",
                    style =
                        MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFD2D2CE),
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF0F0EC))
              }
            }

            Spacer(modifier = Modifier.height(if (isRegisterMode) 18.dp else 22.dp))

            PillActionButton(
                text = if (isRegisterMode) "Ya tengo cuenta" else "Crear cuenta",
                onClick = { isRegisterMode = !isRegisterMode },
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                contentColor = Color(0xFF444443),
                border = BorderStroke(1.dp, Color(0xFFEDEDE8)),
            )
          }
        }
      }

      AuthFooter(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 42.dp))
    }
  }
}

@Composable
private fun FieldLabel(text: String) {
  Text(
      text = text,
      style =
          MaterialTheme.typography.labelSmall.copy(
              color = Color(0xFF7F807E),
              letterSpacing = 2.2.sp,
              fontWeight = FontWeight.SemiBold,
          ),
      modifier = Modifier.padding(start = 14.dp, bottom = 8.dp),
  )
}

@Composable
private fun AuthFooter(modifier: Modifier = Modifier) {
  Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF2C63BA)))
      Box(modifier = Modifier.width(34.dp).height(2.dp).background(Color(0xFFD85858)))
      Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF2C63BA)))
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "PURA VIDA • COSTA RICA",
        style =
            MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFD2D2CE),
                letterSpacing = 2.2.sp,
                fontWeight = FontWeight.Medium,
            ),
    )
  }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
) {
  OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      placeholder = { Text(text = placeholder, color = Color(0xFFD2D2CE)) },
      shape = RoundedCornerShape(16.dp),
      visualTransformation =
          if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
          } else {
            VisualTransformation.None
          },
      trailingIcon =
          if (isPassword && onPasswordVisibilityToggle != null) {
            {
              Icon(
                  imageVector =
                      if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                  contentDescription =
                      if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                  tint = Color(0xFF6F7279),
                  modifier =
                      Modifier.clickable { onPasswordVisibilityToggle() }.padding(end = 4.dp),
              )
            }
          } else {
            null
          },
      colors =
          OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White,
              focusedBorderColor = Color(0xFFE2E3DF),
              unfocusedBorderColor = Color(0xFFE9EAE6),
              focusedTextColor = Color(0xFF2A2C33),
              unfocusedTextColor = Color(0xFF2A2C33),
          ),
  )
}

private fun UserRole.toDisplayName(): String {
  return when (this) {
    UserRole.PASSENGER -> "Pasajero"
    UserRole.DRIVER -> "Conductor"
    UserRole.ADMIN -> "Administrador"
  }
}
