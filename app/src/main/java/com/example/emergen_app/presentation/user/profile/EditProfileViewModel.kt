package com.example.emergen_app.presentation.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val storageRepository: StorageFirebaseRepository
) : ViewModel() {

    private val _editUserState = MutableStateFlow(EditUserState())
    val editUserState: StateFlow<EditUserState> = _editUserState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val password
        get() = _editUserState.value.password

    private val newPassword
        get() = _editUserState.value.newPassword

    fun onUserNameChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(userName = newValue)
    }

    fun onEmailChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(email = newValue)
    }

    fun onMobileChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(mobile = newValue)
    }

    fun onGovernmentChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(governmentName = newValue)
    }

    fun onAreaChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(area = newValue)
    }

    fun onPlotNumberChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(plotNumber = newValue)
    }

    fun onStreetNameChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(streetName = newValue)
    }

    fun onBuildNumberChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(buildNumber = newValue)
    }

    fun onFloorNumberChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(floorNumber = newValue)
    }

    fun onApartmentNumberChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(apartmentNumber = newValue)
    }

    fun onAddressMapsChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(addressMaps = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(password = newValue)
    }

    fun onNewPasswordChange(newValue: String) {
        _editUserState.value = _editUserState.value.copy(newPassword = newValue)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _editUserState.value = _editUserState.value.copy(confirmPassword = newConfirmPassword)
    }

    fun getUserById(userId: String): Flow<User> {
        return storageRepository.getUserById(userId) // استدعاء دالة من Repository
    }

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            try {
                storageRepository.getUserById(userId).collect { fetchedUser ->
                    _editUserState.value = _editUserState.value.copy(
                        userId = fetchedUser.userId,
                        userName = fetchedUser.userName,
                        email = fetchedUser.email,
                        mobile = fetchedUser.mobile,
                        governmentName = fetchedUser.governmentName,
                        area = fetchedUser.area,
                        plotNumber = fetchedUser.plotNumber,
                        streetName = fetchedUser.streetName,
                        buildNumber = fetchedUser.buildNumber,
                        floorNumber = fetchedUser.floorNumber,
                        apartmentNumber = fetchedUser.apartmentNumber,
                        addressMaps = fetchedUser.addressMaps,
                        userPhoto = fetchedUser.userPhoto,
                        idFront = fetchedUser.idFront,
                        idBack = fetchedUser.idBack
                    )
                }
            } catch (e: Exception) {
                _editUserState.value =
                    _editUserState.value.copy(errorMessage = "Failed to fetch user data")
            }
        }
    }

    fun updateUserProfile(navController: NavController) {
        viewModelScope.launch {
            try {
                // التأكد من أنه تم تغيير كلمة السر قبل حفظ البيانات
                if (_editUserState.value.isPasswordChanged) {
                    val updatedUser = User(
                        userId = _editUserState.value.userId,
                        userName = _editUserState.value.userName,
                        email = _editUserState.value.email,
                        mobile = _editUserState.value.mobile,
                        governmentName = _editUserState.value.governmentName,
                        area = _editUserState.value.area,
                        plotNumber = _editUserState.value.plotNumber,
                        streetName = _editUserState.value.streetName,
                        buildNumber = _editUserState.value.buildNumber,
                        floorNumber = _editUserState.value.floorNumber,
                        apartmentNumber = _editUserState.value.apartmentNumber,
                        addressMaps = _editUserState.value.addressMaps,
                        userPhoto = _editUserState.value.userPhoto,
                        idFront = _editUserState.value.idFront,
                        idBack = _editUserState.value.idBack
                    )

                    // حفظ البيانات بعد تغيير كلمة السر
                    storageRepository.updateUserProfile(updatedUser)

                    // الانتقال إلى صفحة تفاصيل الملف الشخصي بعد النجاح
                    navController.popBackStack()
                    navController.navigate(AppDestination.ProfileDetailsDestination.route) {
                        navController.popBackStack()
                    }
                } else {
                    // إذا لم يتم تغيير كلمة السر بنجاح
                    SnackBarManager.showMessage("Please change your password first.")
                }
            } catch (e: Exception) {
                _editUserState.value =
                    _editUserState.value.copy(errorMessage = "Failed to update profile")
            }
        }
    }

    fun changePassword() {
        // التحقق من أن كلمة السر القديمة صحيحة
        if (!password.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.invalid_password_error)
            return
        }

        if (!newPassword.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.invalid_password_error)
            return
        }

        viewModelScope.launch {
            _editUserState.value = _editUserState.value.copy(isLoading = true, error = null)
            try {
                // تغيير كلمة السر في الـ Firebase
                accountRepository.changePassword(newPassword)
                _editUserState.value = _editUserState.value.copy(isPasswordChanged = true)
                SnackBarManager.showMessage(R.string.password_changed_successfully)
            } catch (e: Exception) {
                _editUserState.value = _editUserState.value.copy(isLoading = false, error = e.message)
            } finally {
                _editUserState.value = _editUserState.value.copy(isLoading = false)
            }
        }
    }



}
