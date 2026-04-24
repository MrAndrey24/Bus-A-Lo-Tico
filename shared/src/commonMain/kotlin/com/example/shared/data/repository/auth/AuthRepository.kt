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
