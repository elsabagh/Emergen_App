package com.example.emergen_app.presentation.admin.reports

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
class AdminReportViewModel  @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    private val _helpRequests = MutableStateFlow<List<User>>(emptyList())
    val helpRequests: StateFlow<List<User>> = _helpRequests

    private val _filteredReports = MutableStateFlow<Map<String, List<User>>>(emptyMap())
    val filteredReports: StateFlow<Map<String, List<User>>> = _filteredReports

    init {
        getAllReports()
    }


    fun getAllReports() {
        viewModelScope.launch {
            _helpRequests.value = storageRepository.getAllReports()
        }
    }

    fun updateRequestStatus(userId: String, currentStatus: String) {
        viewModelScope.launch {
            val newStatus = when (currentStatus) {
                "Being Processed" -> "Team On Way"
                "Team On Way" -> "Completed"
                else -> return@launch
            }
            storageRepository.updateReportStatus(userId, newStatus)

            _helpRequests.value = _helpRequests.value.map { request ->
                if (request.userId == userId) request.copy(statusRequest = newStatus) else request
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
