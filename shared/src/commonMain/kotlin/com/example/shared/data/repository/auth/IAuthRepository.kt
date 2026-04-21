/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.auth

interface IAuthRepository {
  suspend fun signIn(email: String, password: String): String

  suspend fun signUp(email: String, password: String): String

  suspend fun signOut()

  fun getCurrentUserId(): String?
}
