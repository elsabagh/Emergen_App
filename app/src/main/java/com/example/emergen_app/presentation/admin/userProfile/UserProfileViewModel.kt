package com.example.emergen_app.presentation.admin.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    fun getUserById(userId: String): Flow<User> {
        return storageRepository.getUserById(userId)
    }

    fun acceptUser(userId: String) {
        viewModelScope.launch {
            try {
                accountRepository.acceptUser(userId)
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error accepting user: ${e.message}")
            }
        }
    }

    fun disableUser(userId: String){
        viewModelScope.launch {
            try {
                accountRepository.disableUser(userId)
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error accepting user: ${e.message}")
            }
        }
    }

    fun rejectUser(userId: String) {
        viewModelScope.launch {
            try {
                accountRepository.rejectUser(userId)
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error rejecting user: ${e.message}")
            }
        }
    }
}
