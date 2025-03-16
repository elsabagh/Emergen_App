package com.example.emergen_app.presentation.admin.branches.editBranch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.data.models.Branch
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
class EditBranchViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageFirebaseRepository: StorageFirebaseRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(EditBranchState())
    val state: StateFlow<EditBranchState> = _state.asStateFlow()

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

    fun onAddressChange(newAddress: String) {
        _state.value = _state.value.copy(address = newAddress)
    }

    fun onAddressMapsChange(newValue: String) {
        _state.value = _state.value.copy(addressMaps = newValue)
    }

    fun onTypeBranchChange(newType: String) {
        _state.value = _state.value.copy(typeBranch = newType)
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

    fun loadBranchData(branchId: String) {
        viewModelScope.launch {
            try {
                Log.d("EditBranchViewModel", "Loading branch data for ID: $branchId") // ✅ Debugging

                val branch = storageFirebaseRepository.getBranchById(branchId)
                branch?.let {
                    _state.value = _state.value.copy(
                        userId = branchId, // ✅ تأكد إن userId بيتخزن
                        branchName = it.branchName,
                        branchCapacity = it.branchCapacity,
                        email = it.email,
                        mobileNumber = it.mobileNumber,
                        address = it.address,
                        password = it.password,
                        workDays = it.workDays,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        addressMaps = it.addressMaps
                    )
                    Log.d("EditBranchViewModel", "Branch loaded successfully: $it") // ✅ Debugging
                }
            } catch (e: Exception) {
                Log.e("EditBranchViewModel", "Error loading branch data: ${e.message}")
                SnackBarManager.showMessage(R.string.generic_error)
            }
        }
    }


    fun updateBranch(navController: NavController) {
        viewModelScope.launch {
            try {
                val updatedBranch = Branch(
                    userId = state.value.userId, // لازم يكون فيه userId!
                    branchName = state.value.branchName,
                    branchCapacity = state.value.branchCapacity,
                    email = state.value.email,
                    mobileNumber = state.value.mobileNumber,
                    address = state.value.address,
                    password = state.value.password,
                    workDays = state.value.workDays,
                    startTime = state.value.startTime,
                    endTime = state.value.endTime,
                    addressMaps = state.value.addressMaps
                )

                Log.d("EditBranchViewModel", "Updating branch: $updatedBranch") // ✅ Debugging

                storageFirebaseRepository.updateBranch(updatedBranch)

                Log.d("EditBranchViewModel", "Branch updated successfully!")
                navController.popBackStack() // ✅ الرجوع للصفحة السابقة بعد الحفظ
            } catch (e: Exception) {
                Log.e("EditBranchViewModel", "Error updating branch: ${e.message}")
                SnackBarManager.showMessage(R.string.account_update_failed)
            }
        }
    }


}
