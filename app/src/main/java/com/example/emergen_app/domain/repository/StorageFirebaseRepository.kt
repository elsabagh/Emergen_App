package com.example.emergen_app.domain.repository

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    suspend fun getUserStatus(email: String): String?

}