package com.example.emergen_app.data.repository


import android.util.Log
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


}
