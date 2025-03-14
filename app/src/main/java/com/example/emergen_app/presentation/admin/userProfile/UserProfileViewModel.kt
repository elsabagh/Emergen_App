package com.example.emergen_app.presentation.admin.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject  // تعديل هنا
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,  // استخدام Repository لعمليات الحساب
    private val storageRepository: StorageFirebaseRepository  // استخدام Repository للتعامل مع Storage
) : ViewModel() {

    // دالة لجلب بيانات المستخدم بناءً على userId
    fun getUserById(userId: String): Flow<User> {
        return storageRepository.getUserById(userId) // استدعاء دالة من Repository
    }

    // تغيير حالة المستخدم إلى accepted
    fun acceptUser(userId: String) {
        viewModelScope.launch {
            try {
                accountRepository.acceptUser(userId)  // نقل المنطق إلى Repository
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error accepting user: ${e.message}")
            }
        }
    }

    // تغيير حالة المستخدم إلى Under Processing
    fun disableUser(userId: String){
        viewModelScope.launch {
            try {
                accountRepository.disableUser(userId)  // نقل المنطق إلى Repository
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error accepting user: ${e.message}")
            }
        }
    }

    // رفض المستخدم
    fun rejectUser(userId: String) {
        viewModelScope.launch {
            try {
                accountRepository.rejectUser(userId)  // نقل المنطق إلى Repository
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error rejecting user: ${e.message}")
            }
        }
    }
}
