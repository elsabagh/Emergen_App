package com.example.emergen_app.presentation.branch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.R
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.data.repository.GraphHopperRepository
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository,
    private val graphHopperRepository: GraphHopperRepository
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

            val currentBranch = _branch.value ?: return@launch

            if (user.statusRequest == "Being Processed") {
                val reportsInTeamOnWay = _helpRequests.value.count {
                    it.statusRequest == "Team On Way" && it.nameBranch == currentBranch.branchName
                }
                if (reportsInTeamOnWay >= currentBranch.branchCapacity.toInt()) {
                    SnackBarManager.showMessage(R.string.the_branch_is_at_full_capacity_cannot_send_more_reports)
                    return@launch
                }
            }


            val lat1 = user.latitude
            val lon1 = user.longitude
            val lat2 = currentBranch.latitude
            val lon2 = currentBranch.longitude
            val apiKey = "d5162feb-126a-4fa4-9aef-6843749d215f"
            Log.d("GraphHopper", "lat1: $lat1, lon1: $lon1, lat2: $lat2, lon2: $lon2")

            graphHopperRepository.getRoute("$lat1,$lon1", "$lat2,$lon2", apiKey).collect { result ->
                result.onSuccess { path ->
                    val distance = "المسافة: ${path.distance / 1000} كم"
                    val duration = "الوقت: ${(path.time / 60000)} دقيقة"
                    val travelInfo = "$distance - $duration"
                    Log.d("GraphHopper", "Distance: ${path.distance}, Duration: ${path.time}")

                    val updatedReport = user.copy(
                        statusRequest = newStatus,
                        nameBranch = currentBranch.branchName,
                        mobileBranch = currentBranch.mobileNumber,
                        addressBranch = currentBranch.addressMaps,
                        betweenAddress = travelInfo
                    )

                    storageRepository.updateReport(updatedReport)
                    _helpRequests.value = _helpRequests.value.map {
                        if (it.userId == user.userId) updatedReport else it
                    }
                }.onFailure {
                    SnackBarManager.showMessage(R.string.error_getting_travel_info)
                }
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