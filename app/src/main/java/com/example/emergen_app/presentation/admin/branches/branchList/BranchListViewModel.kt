package com.example.emergen_app.presentation.admin.branches.branchList

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
class BranchListViewModel @Inject constructor(
    private val repository: StorageFirebaseRepository
) : ViewModel() {

    private val _branches = MutableStateFlow<List<Branch>>(emptyList())
    val branches: StateFlow<List<Branch>> = _branches

    fun fetchBranches(branchName: String) {
        viewModelScope.launch {
            _branches.value = repository.getBranchesByType(branchName)
        }
    }

    fun deleteBranch(branchId: String) {
        viewModelScope.launch {
            try {
                repository.deleteBranchById(branchId)
                fetchBranches(branchName = "branch")
            } catch (e: Exception) {

            }
        }
    }
}