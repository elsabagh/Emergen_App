package com.example.emergen_app.presentation.admin.branches.addBranch


data class AddBranchState(
    val branchName: String = "",
    val branchCapacity: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val address: String = "",
    val typeBranch: String = "",
    val password: String = "",
    val workDays: List<String> = listOf(),
    val startTime: String = "",
    val endTime: String = "",
    val addressMaps: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val isLoading: Boolean = false,  // حالة التحميل أثناء التسجيل
    val isSuccess: Boolean = false,  // حالة النجاح بعد التسجيل
    val errorMessage: String? = null // تخزين أي رسالة خطأ
)
