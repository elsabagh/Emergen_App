package com.example.emergen_app.presentation.user.profile

data class EditUserState(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val mobile: String = "",
    val governmentName: String = "",
    val area: String = "",
    val plotNumber: String = "",
    val streetName: String = "",
    val buildNumber: String = "",
    val floorNumber: String = "",
    val apartmentNumber: String = "",
    val addressMaps: String = "",
    val userPhoto: String = "",
    val idFront: String = "",
    val idBack: String = "",
    val role: String = "user",
    val password: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val statusAccount: String = "accepted",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val error: String? = null,
    val isPasswordChanged: Boolean = false

)