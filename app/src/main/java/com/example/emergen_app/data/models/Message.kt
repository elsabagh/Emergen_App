package com.example.emergen_app.data.models

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "zhovdszVmMfIjwgddeCcU94DRiD3", // الـ Admin User ID
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val nameSender: String = "",
    val nameReceiver: String = "",
    val imageSender: String = "",
)
