package com.example.emergen_app.presentation.signUp


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.AccountRepository
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.utils.isEmailValid
import com.example.emergen_app.utils.isPasswordValid
import com.example.emergen_app.utils.passwordMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.toString

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    private var _isAccountCreated = MutableStateFlow(false)
    val isAccountCreated: StateFlow<Boolean> = _isAccountCreated.asStateFlow()

    fun onUserNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(userName = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }


    fun onMobileChange(newValue: String) {
        _uiState.value = _uiState.value.copy(mobile = newValue)
    }

    fun onGovernmentChange(newValue: String) {
        _uiState.value = _uiState.value.copy(governmentName = newValue)
    }

    fun onAreaChange(newValue: String) {
        _uiState.value = _uiState.value.copy(area = newValue)
    }

    fun onPlotNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(plotNumber = newValue)
    }

    fun onStreetNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(streetName = newValue)
    }

    fun onBuildNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(buildNumber = newValue)
    }

    fun onFloorNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(floorNumber = newValue)
    }

    fun onApartmentNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(apartmentNumber = newValue)
    }

    fun onAddressMapsChange(newValue: String) {
        _uiState.value = _uiState.value.copy(addressMaps = newValue)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue)
    }

    fun createAccount(idFrontUri: Uri?, idBackUri: Uri?, userPhotoUri: Uri?) {
        val state = _uiState.value

        // التحقق من صحة المدخلات
        if (state.userName.isEmpty()) {
            SnackBarManager.showMessage(R.string.empty_name_error)
            return
        }

        if (!state.email.isEmailValid()) {
            SnackBarManager.showMessage(R.string.email_error)
            return
        }

        if (!state.password.isPasswordValid()) {
            SnackBarManager.showMessage(R.string.empty_password_error)
            return
        }

        if (!state.password.passwordMatches(state.confirmPassword)) {
            SnackBarManager.showMessage(R.string.password_match_error)
            return
        }

        if (idFrontUri == null || idBackUri == null || userPhotoUri == null) {
            SnackBarManager.showMessage(R.string.image_upload_error)
            return
        }

        // تعيين حالة التحميل إلى True
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val user = User(
                    userName = state.userName,
                    email = state.email,
                    mobile = state.mobile,
                    governmentName = state.governmentName,
                    area = state.area,
                    plotNumber = state.plotNumber,
                    streetName = state.streetName,
                    buildNumber = state.buildNumber,
                    floorNumber = state.floorNumber,
                    apartmentNumber = state.apartmentNumber,
                    addressMaps = state.addressMaps,
                    role = "user",
                    statusAccount = "Under Processing"
                )

                accountRepository.createAccount(
                    email = state.email,
                    password = state.password,
                    userData = user,
                    idFrontUri = idFrontUri,
                    idBackUri = idBackUri,
                    userPhotoUri = userPhotoUri
                )
                accountRepository.signOut()


                _isAccountCreated.value = true
            } catch (e: Exception) {
                if (e.message?.contains(R.string.email_already_in_use.toString()) == true) {
                    SnackBarManager.showMessage(R.string.email_error)
                } else {
                    SnackBarManager.showMessage(R.string.account_creation_failed)
                }
            } finally {
                // تعيين حالة التحميل إلى False بعد الانتهاء
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun resetIsAccountCreated() {
        _isAccountCreated.value = false
    }
}
