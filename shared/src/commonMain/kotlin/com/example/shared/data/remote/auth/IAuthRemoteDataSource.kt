package com.example.shared.data.remote.auth

interface IAuthRemoteDataSource {
    suspend fun signIn(email: String, password: String): String
    suspend fun signUp(email: String, password: String): String
    suspend fun signOut()
    fun getCurrentUserId(): String?
}
