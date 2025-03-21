package com.example.emergen_app.presentation.branch.branchInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchInfoViewModel@Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository) : ViewModel() {

    private val _branch = MutableStateFlow<Branch?>(null)
    val branch: StateFlow<Branch?> = _branch

    private val _isAccountSignedOut = MutableStateFlow(false)
    val isAccountSignedOut get() = _isAccountSignedOut

    init {
        fetchBranchDetails()
    }

    fun fetchBranchDetails() {
        viewModelScope.launch {
            val branchId = accountRepository.currentUserId
            if (branchId.isNotEmpty()) {
                storageRepository.getBranchUserById(branchId)
                    .onEach {
                        _branch.value = it
                    }
                    .launchIn(viewModelScope)
            }
        }
    }
    
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
