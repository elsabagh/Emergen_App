package com.example.emergen_app.domain.repository

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

}