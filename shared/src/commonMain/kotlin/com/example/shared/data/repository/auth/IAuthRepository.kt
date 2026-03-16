package com.example.shared.data.repository.auth

interface IAuthRepository {
    suspend fun signIn(email: String, password: String): String
    suspend fun signUp(email: String, password: String): String
    suspend fun signOut()
    fun getCurrentUserId(): String?
}
