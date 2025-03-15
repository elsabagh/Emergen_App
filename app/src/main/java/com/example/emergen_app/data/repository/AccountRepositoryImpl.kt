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

    override suspend fun createAccountBranch(
        email: String,
        password: String,
        branchData: Branch
    ) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")

            val branch = branchData.copy(userId = userId)
            val branchDataMap = mapOf(
                "branchName" to branch.branchName,
                "branchCapacity" to branch.branchCapacity,
                "email" to branch.email,
                "mobileNumber" to branch.mobileNumber,
                "workDays" to branch.workDays,
                "startTime" to branch.startTime,
                "endTime" to branch.endTime,
                "userId" to userId,
                "statusAccount" to branch.statusAccount,
                "password" to branch.password
            )

            val userData = mapOf(
                "email" to branch.email,
                "role" to "branch",
                "branch" to branchDataMap
            )
            fireStore.collection("users").document(userId).set(userData).await()

        } catch (e: Exception) {
            throw Exception("Failed to create account branch: ${e.message}", e)
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

    companion object {
        const val LINK_ACCOUNT_TRACE = "linkAccount"

    }

}