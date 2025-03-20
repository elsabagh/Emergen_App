package com.example.emergen_app.domain.repository

import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import kotlinx.coroutines.flow.Flow

interface StorageFirebaseRepository {

    suspend fun getUserRole(email: String): String?

    suspend fun getUserStatus(email: String): String?

    suspend fun getAllUsersWithStatus(status: String): List<User>

    suspend fun getAllReports(): List<User>

    suspend fun getFilteredReportsByBranchType(typeBranch: String): List<User>

    fun getUserById(userId: String): Flow<User>

    fun getBranchUserById(branchId: String): Flow<Branch>

    suspend fun getBranchesByType(typeBranch: String): List<Branch>

    suspend fun getBranchById(branchId: String): Branch?

    suspend fun updateBranch(branch: Branch)

    suspend fun updateUserProfile(updatedUser: User)

    suspend fun createHelpRequest(user: User)

    suspend fun getAllHelpRequests(): List<User>
}