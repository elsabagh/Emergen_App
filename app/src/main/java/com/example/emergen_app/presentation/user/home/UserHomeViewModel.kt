package com.example.emergen_app.presentation.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {
    // حالة الطلب: نجاح أو فشل
    private val _helpRequestStatus = MutableStateFlow<Result>(Result.Pending)
    val helpRequestStatus: StateFlow<Result> = _helpRequestStatus

    // حالة بيانات المستخدم
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _user.value = accountRepository.getCurrentUser()
        }
    }

    // دالة لإنشاء طلب الاستغاثة
    fun createUrgentHelpRequest(user: User) {
        viewModelScope.launch {
            try {
                storageRepository.createUrgentHelpRequest(user)
                _helpRequestStatus.value = Result.Success
                SnackBarManager.showMessage(R.string.your_help_request_was_successful)
            } catch (e: Exception) {
                _helpRequestStatus.value = Result.Failure(e.message ?: "Unknown Error")
            }
        }
    }

    // أنواع الحالة: Pending، Success، Failure
    sealed class Result {
        object Pending : Result()
        object Success : Result()
        data class Failure(val errorMessage: String) : Result()
    }
}