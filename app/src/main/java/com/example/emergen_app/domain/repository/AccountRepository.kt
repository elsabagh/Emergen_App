package com.example.emergen_app.domain.repository

import android.net.Uri
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val currentUser: Flow<User>

    val currentUserId: String

    val isUserSignedIn: Boolean

    suspend fun authenticate(email: String, password: String)

    suspend fun createAccount(
        email: String,
        password: String,
        userData: User,
        idFrontUri: Uri?,
        idBackUri: Uri?,
        userPhotoUri: Uri?
    )

    suspend fun changePassword(newPassword: String)

    suspend fun linkAccount(email: String, password: String)

    suspend fun deleteAccount()

    suspend fun signOut()

    suspend fun getCurrentUserEmail(): String? // ✅ إضافة هذه الدالة

    suspend fun getCurrentUser(): User?

    suspend fun acceptUser(userId: String)

    suspend fun disableUser(userId: String)

    suspend fun rejectUser(userId: String)

    suspend fun createAccountBranch(
        email: String,
        password: String,
        branchData: Branch
    )

    suspend fun updateUserProfile(
        userId: String,
        updatedUserData: User,
        userPhotoUri: Uri?,
        idFrontUri: Uri?,
        idBackUri: Uri?
    )
}