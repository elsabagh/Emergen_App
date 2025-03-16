package com.example.emergen_app.presentation.admin.branches.branchList.branchDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchDetailsViewModel @Inject constructor(
    private val repository: StorageFirebaseRepository
) : ViewModel() {

    private val _branch = MutableStateFlow(Branch())
    val branch: StateFlow<Branch> = _branch

    fun fetchBranchDetails(branchId: String) {
        viewModelScope.launch {
            val branchData = repository.getBranchById(branchId)
            _branch.value = branchData ?: Branch()
        }
    }
}
