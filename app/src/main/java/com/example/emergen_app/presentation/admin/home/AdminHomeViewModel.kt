package com.example.emergen_app.presentation.admin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _isAccountSignedOut = MutableStateFlow(false)
    val isAccountSignedOut get() = _isAccountSignedOut


    fun signOutFromAccount() {
        viewModelScope.launch {
            accountRepository.signOut()
            _isAccountSignedOut.value = true
        }
    }

    fun resetIsAccountSignedOut() {
        _isAccountSignedOut.value = false
    }
}