package com.example.emergen_app.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.ui.util.trace
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AccountRepository {
    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(
                    auth.currentUser?.let {
                        User(
                            userId = it.uid,
                        )
                    } ?: User()
                )
            }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(listener)
            }
        }

    override val currentUserId: String
        get() = firebaseAuth.currentUser?.uid.orEmpty()

    override val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    // داخل AccountRepositoryImpl
    override suspend fun getCurrentUser(): User? {
        val userId = firebaseAuth.currentUser?.uid ?: return null
        return try {
            val userDoc = fireStore.collection("users").document(userId).get().await()
            userDoc.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching current user: ${e.message}")
            null
        }
    }

    override suspend fun getCurrentBranch(): Branch? {
       val branchId = firebaseAuth.currentUser?.uid ?: return null
        return try {
            val branchDoc = fireStore.collection("users").document(branchId).get().await()
            branchDoc.toObject(Branch::class.java)
        } catch (e: Exception) {
            Log.e("AccountRepository", "Error fetching current branch: ${e.message}")
            null
        }
    }

    override fun getUserById(userId: String): Flow<User> {
        return flow {
            try {
                val userDoc = fireStore.collection("users").document(userId).get().await()
                val user = userDoc.toObject(User::class.java)

                if (user != null) {
                    Log.d("FirebaseReposs", "User found: $userId")
                    emit(user)  // إرسال بيانات المستخدم
                } else {
                    Log.d("FirebaseReposs", "No user found with ID: $userId")
                    emit(User())  // في حالة عدم العثور على المستخدم، إرسال كائن فارغ
                }
            } catch (e: Exception) {
                Log.e("FirebaseReposs", "Error fetching user by ID: ${e.message}")
                emit(User())  // في حالة حدوث خطأ، إرسال كائن فارغ
            }
        }
    }



    override suspend fun authenticate(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account creation: ${e.message}", e)
        }
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        userData: User,
        idFrontUri: Uri?,
        idBackUri: Uri?,
        userPhotoUri: Uri?
    ) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")

            val idFrontUrl = idFrontUri?.let { uploadImageToStorage(userId, it, "id_front") }
            val idBackUrl = idBackUri?.let { uploadImageToStorage(userId, it, "id_back") }
            val userPhotoUrl = userPhotoUri?.let { uploadImageToStorage(userId, it, "user_photo") }

            val user = userData.copy(
                userId = userId,
                idFront = idFrontUrl.orEmpty(),
                idBack = idBackUrl.orEmpty(),
                userPhoto = userPhotoUrl.orEmpty()
            )
            fireStore.collection("users").document(userId).set(user).await()

        } catch (e: Exception) {
            throw Exception("Failed to create account: ${e.message}", e)
        }
    }

    override suspend fun changePassword(newPassword: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            try {
                user.updatePassword(newPassword).await()
            } catch (e: Exception) {
                throw Exception("Failed to change password: ${e.message}", e)
            } catch (e: IOException) {
                throw IOException("Network error occurred during password change: ${e.message}", e)
            }
        } else {
            throw Exception("No authenticated user found")
        }
    }

    override suspend fun createAccountBranch(
        email: String,
        password: String,
        branchData: Branch
    ) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")

            val branch = branchData.copy(
                userId = userId
            )

            fireStore.collection("users").document(userId).set(branch).await()

        } catch (e: Exception) {
            throw Exception("Failed to create account branch: ${e.message}", e)
        }
    }

    // دالة لإنشاء الحساب (تعديلها لتكون أكثر توافقًا مع التحديثات الجديدة)
    override suspend fun updateUserProfile(
        userId: String,
        updatedUserData: User,
        userPhotoUri: Uri?,
        idFrontUri: Uri?,
        idBackUri: Uri?
    ) {
        try {
            // رفع الصور وتخزين روابطها
            val userPhotoUrl = userPhotoUri?.let { uploadImageToStorage(userId, it, "user_photo") }
            val idFrontUrl = idFrontUri?.let { uploadImageToStorage(userId, it, "id_front") }
            val idBackUrl = idBackUri?.let { uploadImageToStorage(userId, it, "id_back") }

            // إنشاء نسخة جديدة للمستخدم مع الروابط المحدثة
            val user = updatedUserData.copy(
                userId = userId,
                userPhoto = userPhotoUrl.orEmpty(),
                idFront = idFrontUrl.orEmpty(),
                idBack = idBackUrl.orEmpty()
            )

            // تحديث بيانات المستخدم في Firestore
            fireStore.collection("users").document(userId).set(user).await()

        } catch (e: Exception) {
            throw Exception("Failed to update profile: ${e.message}", e)
        }
    }

    private suspend fun uploadImageToStorage(
        userId: String,
        fileUri: Uri,
        fileName: String
    ): String {
        return try {
            val ref = storage.reference.child("users/$userId/$fileName.jpg")
            ref.putFile(fileUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error uploading image: $fileName", e)
            throw Exception("Failed to link account: ${e.message}", e)
        }
    }


    override suspend fun linkAccount(email: String, password: String) {
        try {
            trace(LINK_ACCOUNT_TRACE) {
                val credential = EmailAuthProvider.getCredential(email, password)
                firebaseAuth.currentUser!!.linkWithCredential(credential).await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to link account: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account linking: ${e.message}", e)
        }
    }

    override suspend fun deleteAccount() {
        try {
            firebaseAuth.currentUser!!.delete().await()
        } catch (e: Exception) {
            throw Exception("Failed to delete account: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account deletion: ${e.message}", e)
        }
    }

    override suspend fun signOut() {
        try {
            if (firebaseAuth.currentUser!!.isAnonymous) {
                firebaseAuth.currentUser!!.delete()
            }
            firebaseAuth.signOut()
        } catch (e: Exception) {
            throw Exception("Failed to sign out: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during sign-out: ${e.message}", e)
        }
    }

    // تحديث حالة المستخدم إلى accepted
    override suspend fun acceptUser(userId: String) {
        val userRef = fireStore.collection("users").document(userId)
        userRef.update("statusAccount", "accepted").await()
    }

    // تحديث حالة المستخدم إلى accepted
    override suspend fun disableUser(userId: String) {
        val userRef = fireStore.collection("users").document(userId)
        userRef.update("statusAccount", "Under Processing").await()
    }


    // حذف المستخدم من Firestore و FirebaseAuth
    override suspend fun rejectUser(userId: String) {
        // الحصول على بيانات المستخدم من Firestore
        val userRef = fireStore.collection("users").document(userId)
        val user = userRef.get().await().toObject(User::class.java)

        user?.let {
            try {
                // حذف الصور من Firebase Storage
                deleteUserImagesFromStorage(userId)

                // حذف المستخدم من Firestore
                userRef.delete().await()

                // حذف الحساب من FirebaseAuth
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser?.uid == userId) {
                    firebaseUser.delete().await() // حذف الحساب من FirebaseAuth
                }

                Log.d("UserProfile", "User account rejected and deleted successfully.")
            } catch (e: Exception) {
                Log.e("UserProfile", "Error rejecting user: ${e.message}")
            }
        } ?: Log.e("UserProfile", "User not found in Firestore.")
    }


    // حذف الصور من Firebase Storage
    private suspend fun deleteUserImagesFromStorage(userId: String) {
        try {
            // تحديد مسارات الصور المرتبطة بالحساب
            val idFrontRef = storage.reference.child("users/$userId/id_front.jpg")
            val idBackRef = storage.reference.child("users/$userId/id_back.jpg")
            val userPhotoRef = storage.reference.child("users/$userId/user_photo.jpg")

            // تحقق من وجود الصورة قبل حذفها
            try {
                idFrontRef.delete().await()
                Log.d("FirebaseStorage", "ID Front image deleted successfully.")
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "Error deleting ID Front image: ${e.message}")
            }

            try {
                idBackRef.delete().await()
                Log.d("FirebaseStorage", "ID Back image deleted successfully.")
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "Error deleting ID Back image: ${e.message}")
            }

            try {
                userPhotoRef.delete().await()
                Log.d("FirebaseStorage", "User photo image deleted successfully.")
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "Error deleting User photo image: ${e.message}")
            }

        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error deleting user images: ${e.message}")
        }
    }

    override suspend fun updateUserEmail(currentPassword: String, newEmail: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("No authenticated user found"))

        return try {
            // إعادة التحقق من هوية المستخدم
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()

            // تحديث الإيميل في Firebase Auth
            user.updateEmail(newEmail).await()

            // تحديث الإيميل في Firestore
            fireStore.collection("users").document(user.uid)
                .update("email", newEmail).await()

            // إعادة تسجيل الدخول لضمان استمرارية الجلسة
            firebaseAuth.signOut()
            firebaseAuth.signInWithEmailAndPassword(newEmail, currentPassword).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val LINK_ACCOUNT_TRACE = "linkAccount"

    }

}