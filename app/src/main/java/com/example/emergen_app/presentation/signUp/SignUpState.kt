package com.example.emergen_app.presentation.signUp

data class SignUpState(
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
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,  // حالة التحميل أثناء التسجيل
    val isSuccess: Boolean = false,  // حالة النجاح بعد التسجيل
    val errorMessage: String? = null // تخزين أي رسالة خطأ
)
