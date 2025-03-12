package com.example.emergen_app.presentation.admin.accounts


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val storageFirebaseRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<User>>(emptyList())
    val accounts: StateFlow<List<User>> = _accounts

    init {
        loadAcceptedAccounts()
    }

    private fun loadAcceptedAccounts() {
        viewModelScope.launch {
            // استرجاع الحسابات التي تحتوي على حالة "accepted"
            val acceptedAccounts = storageFirebaseRepository.getAllUsersWithStatus("accepted")
            _accounts.value = acceptedAccounts
        }
    }
}
