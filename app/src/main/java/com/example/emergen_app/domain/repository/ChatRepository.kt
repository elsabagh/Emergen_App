package com.example.emergen_app.domain.repository

import com.example.emergen_app.data.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: Message)

    fun getUserMessages(): Flow<List<Message>>

    fun getChatWithUser(userId: String): Flow<List<Message>>

    fun getChatWithCurrentUser(): Flow<List<Message>>
}