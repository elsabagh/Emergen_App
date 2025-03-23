package com.example.emergen_app.data.models

data class Branch(
    val branchName: String = "",
    val branchCapacity: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val password: String = "",
    val workDays: List<String> = listOf(),
    val startTime: String = "",
    val endTime: String = "",
    val userId: String = "",
    val address: String = "",
    val statusAccount: String = "accepted",
    val role: String = "branch",
    val typeBranch: String = "",
    val addressMaps: String = "",
    val latitude: String = "",
    val longitude: String = "",
)
