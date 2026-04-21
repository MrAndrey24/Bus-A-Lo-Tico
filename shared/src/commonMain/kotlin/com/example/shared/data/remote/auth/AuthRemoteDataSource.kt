/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

class AuthRemoteDataSource : IAuthRemoteDataSource {

  override suspend fun signIn(email: String, password: String): String {
    val result = Firebase.auth.signInWithEmailAndPassword(email, password)
    return result.user?.uid ?: error("No se pudo obtener el uid del usuario autenticado")
  }

  override suspend fun signUp(email: String, password: String): String {
    val result = Firebase.auth.createUserWithEmailAndPassword(email, password)
    return result.user?.uid ?: error("No se pudo obtener el uid del usuario registrado")
  }

  override suspend fun signOut() {
    Firebase.auth.signOut()
  }

  override fun getCurrentUserId(): String? {
    return Firebase.auth.currentUser?.uid
  }
}
