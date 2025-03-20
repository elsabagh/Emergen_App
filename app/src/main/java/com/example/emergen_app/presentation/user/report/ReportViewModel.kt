package com.example.emergen_app.presentation.user.report

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
class ReportViewModel @Inject constructor(
    private val repository: StorageFirebaseRepository
) : ViewModel() {

    private val _helpRequests = MutableStateFlow<List<User>>(emptyList())
    val helpRequests: StateFlow<List<User>> = _helpRequests

    init {
        getHelpRequests()
    }

    fun getHelpRequests() {
        viewModelScope.launch {
            _helpRequests.value = repository.getAllHelpRequests()
        }
    }

}