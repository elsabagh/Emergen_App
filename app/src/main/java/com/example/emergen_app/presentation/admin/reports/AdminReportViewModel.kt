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

    // حالة المستخدم من التقرير
    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    private val _helpRequests = MutableStateFlow<List<User>>(emptyList())
    val helpRequests: StateFlow<List<User>> = _helpRequests

    private val _filteredReports = MutableStateFlow<Map<String, List<User>>>(emptyMap()) // Key: statusRequest, Value: List of Users
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
                else -> return@launch // لا يتم التحديث إذا كان بالفعل "Completed"
            }
            storageRepository.updateReportStatus(userId, newStatus)

            // تحديث الحالة محليًا في القائمة لضمان التحديث الفوري دون الحاجة إلى إعادة تحميل كاملة
            _helpRequests.value = _helpRequests.value.map { request ->
                if (request.userId == userId) request.copy(statusRequest = newStatus) else request
            }
        }
    }

    fun getReportById(reportId: String) {
        viewModelScope.launch {
            val report = storageRepository.getReportById(reportId)  // جلب التقرير
            _userDetails.value = report  // حفظ بيانات المستخدم داخل _userDetails
        }
    }
}
