package com.example.emergen_app.data.models

data class User(
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
    var userPhoto: String = "",
    var idFront: String = "",
    var idBack: String = "",
    val role: String = "user",
    val statusAccount: String = "pending"
)