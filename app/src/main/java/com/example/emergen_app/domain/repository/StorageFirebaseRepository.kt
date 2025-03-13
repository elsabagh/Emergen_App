package com.example.emergen_app.domain.repository

import com.example.emergen_app.data.models.User
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    suspend fun getUserStatus(email: String): String?

    suspend fun getAllUsersWithStatus(status: String): List<User>

     fun getUserById(userId: String): Flow<User>


}