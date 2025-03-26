package com.example.emergen_app.presentation.admin.accounts


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val storageFirebaseRepository: StorageFirebaseRepository,
    private val accountRepository: AccountRepository

) : ViewModel() {

    private val _accounts = MutableStateFlow<List<User>>(emptyList())
    val accounts: StateFlow<List<User>> = _accounts

    init {
        loadAcceptedAccounts()
    }

    private fun loadAcceptedAccounts() {
        viewModelScope.launch {
            val acceptedAccounts = storageFirebaseRepository.getAllUsersWithStatus("accepted")
            _accounts.value = acceptedAccounts
        }
    }

    fun deleteAccount(userId: String) {
        viewModelScope.launch {
            accountRepository.rejectUser(userId)
            loadAcceptedAccounts()
        }
    }
}
