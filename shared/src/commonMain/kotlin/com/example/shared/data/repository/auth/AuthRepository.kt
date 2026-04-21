/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.repository.auth

import com.example.shared.data.remote.auth.IAuthRemoteDataSource

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
