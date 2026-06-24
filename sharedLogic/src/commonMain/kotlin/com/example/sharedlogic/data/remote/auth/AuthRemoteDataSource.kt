/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.data.remote.auth

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
