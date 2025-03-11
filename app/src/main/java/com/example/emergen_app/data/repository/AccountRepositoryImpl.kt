package com.example.emergen_app.data.repository

import androidx.compose.ui.util.trace
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
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

    override suspend fun createAccount(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw Exception("Failed to create account: ${e.message}", e)
        } catch (e: IOException) {
            throw IOException("Network error occurred during account creation: ${e.message}", e)
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

    companion object {
        const val LINK_ACCOUNT_TRACE = "linkAccount"

    }

}