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
package com.example.sharedlogic.data.repository.auth

import com.example.sharedlogic.data.remote.auth.IAuthRemoteDataSource

class AuthRepository(private val remoteDataSource: IAuthRemoteDataSource) : IAuthRepository {

  override suspend fun signIn(email: String, password: String): String {
    return remoteDataSource.signIn(email, password)
  }

  override suspend fun signUp(email: String, password: String): String {
    return remoteDataSource.signUp(email, password)
  }

  override suspend fun signOut() {
    remoteDataSource.signOut()
  }

  override fun getCurrentUserId(): String? {
    return remoteDataSource.getCurrentUserId()
  }
}
