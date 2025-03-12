package com.example.emergen_app.presentation.admin.notification

import android.util.Log
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
class NotificationViewModel @Inject constructor(
    private val storageFirebaseRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<User>>(emptyList())
    val notifications: StateFlow<List<User>> = _notifications

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            // جلب المستخدمين الذين لديهم الحالة "Under Processing" و الدور "user"
            val userNotifications = storageFirebaseRepository.getAllUsersWithStatus("Under Processing")
            Log.d("NotificationViewModel", "Loaded Notifications: $userNotifications")
            _notifications.value = userNotifications
        }
    }

}
