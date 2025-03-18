package com.example.emergen_app.domain.repository

import android.net.Uri
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    suspend fun getUserStatus(email: String): String?

    suspend fun getAllUsersWithStatus(status: String): List<User>

    fun getUserById(userId: String): Flow<User>

    suspend fun getBranchesByType(typeBranch: String): List<Branch>

    suspend fun getBranchById(branchId: String): Branch? // ✅ دالة جديدة لجلب بيانات الفرع

    suspend fun updateBranch(branch: Branch)

    suspend fun updateUserProfile(updatedUser: User)

    suspend fun uploadImageToStorage(userId: String, fileUri: Uri, fileName: String): String

    suspend fun createUrgentHelpRequest(user: User)
}