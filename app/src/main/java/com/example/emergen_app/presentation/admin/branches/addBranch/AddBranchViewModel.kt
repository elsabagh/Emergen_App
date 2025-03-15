package com.example.emergen_app.presentation.admin.branches.addBranch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBranchViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddBranchState())
    val state: StateFlow<AddBranchState> = _state.asStateFlow()

    fun onBranchNameChange(newName: String) {
        _state.value = _state.value.copy(branchName = newName)
    }

    fun onBranchCapacityChange(newCapacity: String) {
        _state.value = _state.value.copy(branchCapacity = newCapacity)
    }

    fun onEmailChange(newEmail: String) {
        _state.value = _state.value.copy(email = newEmail)
    }

    fun onPasswordChange(newValue: String) {
        _state.value = _state.value.copy(password = newValue)
    }

    fun onMobileNumberChange(newMobileNumber: String) {
        _state.value = _state.value.copy(mobileNumber = newMobileNumber)
    }

    fun onWorkDaysChange(selectedDays: List<String>) {
        _state.value = _state.value.copy(workDays = selectedDays)
    }

    fun onStartTimeChange(newStartTime: String) {
        _state.value = _state.value.copy(startTime = newStartTime)
    }

    fun onEndTimeChange(newEndTime: String) {
        _state.value = _state.value.copy(endTime = newEndTime)
    }

    fun createBranchAccount(navController: NavController) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val branch = Branch(
                    branchName = state.value.branchName,
                    branchCapacity = state.value.branchCapacity,
                    email = state.value.email,
                    mobileNumber = state.value.mobileNumber,
                    workDays = state.value.workDays,
                    startTime = state.value.startTime,
                    endTime = state.value.endTime,
                    statusAccount = "accepted",
                    userId = "",
                    role = "branch"
                )

                accountRepository.createAccountBranch(
                    state.value.email,
                    state.value.password,
                    branch
                )

                accountRepository.signOut()
                accountRepository.authenticate("admin@admin.com", "123456")

                SnackBarManager.showMessage(R.string.created_account)

                navController.navigate(AppDestination.AllBranchesDestination.route) {
                    popUpTo(AppDestination.AllBranchesDestination.route) { inclusive = true }
                }

            } catch (e: Exception) {
                if (e.message?.contains(R.string.email_already_in_use.toString()) == true) {
                    SnackBarManager.showMessage(R.string.email_error)
                } else {
                    SnackBarManager.showMessage(R.string.account_creation_failed)
                }
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}
