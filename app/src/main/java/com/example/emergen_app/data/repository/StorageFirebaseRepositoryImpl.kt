package com.example.emergen_app.data.repository


import android.util.Log
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageFirebaseRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : StorageFirebaseRepository {

    override suspend fun getUserRole(email: String): String? {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val role = if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].getString("role")
            } else {
                null
            }
            Log.d("FirebaseRepo", "User $email role: $role")
            role
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching user role", e)
            null
        }
    }

    override suspend fun getUserStatus(email: String): String? {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val status = if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].getString("statusAccount")
            } else {
                null
            }
            Log.d("FirebaseRepo", "User $email status: $status")
            status
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching user status", e)
            null
        }
    }

    override suspend fun getAllUsersWithStatus(status: String): List<User> {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("statusAccount", status)
                .whereEqualTo("role", "user")
                .get()
                .await()

            // إذا كانت النتيجة غير فارغة، طباعة الوثائق المسترجعة
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.forEach { document ->
                    Log.d("FirebaseRepo", "User Data: ${document.data}")
                }
            }

            querySnapshot.documents.mapNotNull { doc ->
                val user = doc.toObject(User::class.java)
                user?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching users with status: $status", e)
            emptyList<User>()
        }
    }

    // جلب بيانات المستخدم من Firestore
    override fun getUserById(userId: String): Flow<User> {
        return flow {
            val userDoc = fireStore.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)
            emit(user ?: User())  // إرسال بيانات المستخدم
        }
    }

    override suspend fun getBranchesByType(typeBranch: String): List<Branch> {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("role", "branch")  // ✅ جلب الفروع فقط
                .whereEqualTo("typeBranch", typeBranch)  // ✅ الفلاتر حسب `typeBranch`
                .get()
                .await()

            // إذا كانت النتيجة غير فارغة، طباعة الوثائق المسترجعة
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.forEach { document ->
                    Log.d("FirebaseRepo", "User Data: ${document.data}")
                }
            }

            querySnapshot.documents.mapNotNull { doc ->
                val branch = doc.toObject(Branch::class.java)
                branch?.copy(userId = doc.id)  // ✅ إضافة `userId` إلى بيانات الفرع
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching branches of type: $typeBranch", e)
            emptyList<Branch>()
        }
    }

    override suspend fun getBranchById(branchId: String): Branch? {
        return try {
            val docSnapshot = fireStore.collection("users")
                .document(branchId)  // 🔥 جلب البيانات من الوثيقة التي تحتوي على branchId
                .get()
                .await()

            if (docSnapshot.exists()) {
                docSnapshot.toObject(Branch::class.java)?.copy(userId = branchId)  // ✅ تحويل البيانات إلى كائن Branch
            } else {
                null  // ❌ لا يوجد فرع بهذا الـ ID
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching branch by ID: $branchId", e)
            null
        }
    }
    override suspend fun updateBranch(branch: Branch) {
        try {
            val branchRef = fireStore.collection("users").document(branch.userId)

            Log.d("FirebaseRepo", "Attempting to update branch: ${branch.userId}") // ✅ Debugging
            Log.d("FirebaseRepo", "Data to be updated: $branch") // ✅ Debugging

            // تحديث البيانات
            branchRef.update(
                mapOf(
                    "branchName" to branch.branchName,
                    "branchCapacity" to branch.branchCapacity,
                    "email" to branch.email,
                    "mobileNumber" to branch.mobileNumber,
                    "address" to branch.address,
                    "password" to branch.password,
                    "workDays" to branch.workDays,
                    "startTime" to branch.startTime,
                    "endTime" to branch.endTime,
                    "addressMaps" to branch.addressMaps
                )
            ).await()

            Log.d("FirebaseRepo", "Branch ${branch.userId} updated successfully.")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating branch ${branch.userId}: ${e.message}")
            throw Exception("Failed to update branch: ${e.message}", e)
        }
    }




}
