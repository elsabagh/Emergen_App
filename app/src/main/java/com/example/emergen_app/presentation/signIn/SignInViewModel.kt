package com.example.emergen_app.presentation.signIn

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.R
import com.example.emergen_app.data.repository.StorageFirebaseRepositoryImpl
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageFirebaseRepository: StorageFirebaseRepositoryImpl

) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val email
        get() = _uiState.value.email

    private val password
        get() = _uiState.value.password

    private val _isSignInSucceeded = MutableStateFlow(false)
    val isSignInSucceeded: StateFlow<Boolean> = _isSignInSucceeded.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private var _isAccountReady = MutableStateFlow(false)
    val isAccountReady: StateFlow<Boolean> = _isAccountReady

    init {
        loadUserRole()
    }

    private fun loadUserRole() {
        viewModelScope.launch {
            if (accountRepository.isUserSignedIn) {
                val email = accountRepository.getCurrentUserEmail()
                if (email != null) {
                    _userRole.value = storageFirebaseRepository.getUserRole(email)
                }
            }
            _isAccountReady.value = true // ✅ يتم تحديث الحالة بعد استرجاع البيانات
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }
    private fun String.isEmailValid() =
        this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


    fun signInToAccount(selectedRole: String) {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (!email.isEmailValid()) {
            SnackBarManager.showMessage(R.string.email_error)
            return
        }
        if (password.isBlank()) {
            SnackBarManager.showMessage(R.string.password_error)
            return
        }

        viewModelScope.launch {
            try {
                // التوثيق
                accountRepository.authenticate(email, password)
                validateUserRoleAndStatus(email, selectedRole)
            } catch (e: Exception) {
                SnackBarManager.showMessage(R.string.authentication_failed)
            }
        }
    }

    private suspend fun validateUserRoleAndStatus(email: String, selectedRole: String) {
        // جلب الدور من Firebase
        val role = storageFirebaseRepository.getUserRole(email)
        val status = storageFirebaseRepository.getUserStatus(email) // جلب حالة الحساب

        _userRole.value = role

        // التحقق من الدور وحالة الحساب
        if (role == selectedRole) {
            if (status == "accepted") {
                _isSignInSucceeded.value = true
            } else {
                SnackBarManager.showMessage(R.string.account_status_not_accepted) // رسالة خطأ بناءً على حالة الحساب
            }
        } else {
            SnackBarManager.showMessage(R.string.invalid_role_error)
        }
    }

    fun resetIsSignInSucceeded() {
        _isSignInSucceeded.value = false
    }

}