package com.example.emergen_app.presentation.user.profile

import android.net.Uri
import android.util.Log
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
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    fun onEmailChange(newEmail: String) {
        _editUserState.value = _editUserState.value.copy(email = newEmail)
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

    fun updateUserEmail(
        currentPassword: String,
        newEmail: String,
        navController: NavController,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = accountRepository.updateUserEmail(currentPassword, newEmail)
            result.fold(
                onSuccess = {
                    // ✅ تحديث التنقل بعد نجاح تغيير الإيميل
                    navController.popBackStack()
                    navController.navigate(AppDestination.ProfileDetailsDestination.route) {
                        navController.popBackStack()
                    }

                    onSuccess()
                },
                onFailure = { e -> onFailure(e.message ?: "Failed to update email") }
            )
        }
    }




    fun updateUserProfile(navController: NavController, newUserPhotoUri: Uri?, newIdFrontUri: Uri?, newIdBackUri: Uri?) {
        viewModelScope.launch {
            try {

                val newUserPhotoUrl = newUserPhotoUri?.let {
                    uploadImageToStorage(_editUserState.value.userId, it, "user_photo")
                } ?: _editUserState.value.userPhoto

                val newIdFrontUrl = newIdFrontUri?.let {
                    uploadImageToStorage(_editUserState.value.userId, it, "id_front")
                } ?: _editUserState.value.idFront

                val newIdBackUrl = newIdBackUri?.let {
                    uploadImageToStorage(_editUserState.value.userId, it, "id_back")
                } ?: _editUserState.value.idBack

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
                    userPhoto = newUserPhotoUrl,
                    idFront = newIdFrontUrl,
                    idBack = newIdBackUrl,
                )

                storageRepository.updateUserProfile(updatedUser)

                _editUserState.value = _editUserState.value.copy(
                    userPhoto = newUserPhotoUrl,
                    idFront = newIdFrontUrl,
                    idBack = newIdBackUrl
                )

                navController.popBackStack()
                navController.navigate(AppDestination.ProfileDetailsDestination.route) {
                    navController.popBackStack()
                }

            } catch (e: Exception) {
                _editUserState.value = _editUserState.value.copy(errorMessage = "Failed to update profile")
            }
        }
    }


    private suspend fun uploadImageToStorage(userId: String, fileUri: Uri, fileName: String): String {
        return try {
            val ref = FirebaseStorage.getInstance().reference.child("users/$userId/$fileName.jpg")
            ref.putFile(fileUri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error uploading image: $fileName", e)
            throw Exception("Failed to upload image", e)
        }
    }

    fun changePassword() {

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
                accountRepository.changePassword(newPassword)
                _editUserState.value = _editUserState.value.copy(isPasswordChanged = true)
                SnackBarManager.showMessage(R.string.password_changed_successfully)
            } catch (e: Exception) {
                _editUserState.value =
                    _editUserState.value.copy(isLoading = false, error = e.message)
            } finally {
                _editUserState.value = _editUserState.value.copy(isLoading = false)
            }
        }
    }

}
