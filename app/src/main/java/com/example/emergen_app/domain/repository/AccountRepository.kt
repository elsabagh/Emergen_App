package com.example.emergen_app.domain.repository

import com.example.emergen_app.data.models.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val currentUser: Flow<User>

    val currentUserId: String

    val isUserSignedIn: Boolean

    suspend fun authenticate(email: String, password: String)

    suspend fun createAccount(email: String, password: String)

    suspend fun linkAccount(email: String, password: String)

    suspend fun deleteAccount()

    suspend fun signOut()

    suspend fun getCurrentUserEmail(): String? // ✅ إضافة هذه الدالة

}