package com.example.emergen_app.presentation.branch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _branch = MutableStateFlow<Branch?>(null)
    val branch: StateFlow<Branch?> = _branch

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    private val _helpRequests = MutableStateFlow<List<User>>(emptyList())
    val helpRequests: StateFlow<List<User>> = _helpRequests

    private val _filteredReports = MutableStateFlow<Map<String, List<User>>>(emptyMap())
    val filteredReports: StateFlow<Map<String, List<User>>> = _filteredReports

    init {
        loadCurrentBranch()
    }

    private fun loadCurrentBranch() {
        viewModelScope.launch {
            _branch.value = accountRepository.getCurrentBranch()
        }
    }


    fun getAllReports() {
        viewModelScope.launch {
            _helpRequests.value = storageRepository.getAllReports()
        }
    }


    fun getFilteredReports(typeBranch: String) {
        viewModelScope.launch {
            _helpRequests.value = storageRepository.getFilteredReportsByBranchType(typeBranch)
        }
    }

    fun getFilteredReportsType(typeBranch: String, branchName: String) {
        viewModelScope.launch {
            _helpRequests.value =
                storageRepository.getFilteredReportsByBranchTypeAndName(typeBranch, branchName)
        }
    }


    fun updateRequestStatus(user: User) {
        viewModelScope.launch {
            val newStatus = when (user.statusRequest) {
                "Being Processed" -> "Team On Way"
                "Team On Way" -> "Completed"
                else -> return@launch
            }

            val currentBranch = _branch.value

            if (currentBranch == null) {
                return@launch
            }

            val updatedReport = user.copy(
                statusRequest = newStatus,
                nameBranch = currentBranch.branchName,
                mobileBranch = currentBranch.mobileNumber,
                addressBranch = currentBranch.addressMaps,
                betweenAddress = ""
            )

            storageRepository.updateReport(updatedReport)

            _helpRequests.value = _helpRequests.value.map {
                if (it.userId == user.userId) updatedReport else it
            }
        }
    }


    fun getReportById(reportId: String) {
        viewModelScope.launch {
            val report = storageRepository.getReportById(reportId)
            _userDetails.value = report
        }
    }
}