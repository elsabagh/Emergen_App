package com.example.emergen_app.data.repository

import com.example.emergen_app.data.models.Message
import com.example.emergen_app.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ChatRepository {

    // إرسال رسالة
    override suspend fun sendMessage(message: Message) {
        try {
            fireStore.collection("messages")
                .add(message)
                .await()
        } catch (e: Exception) {
            throw Exception("Failed to send message: ${e.message}", e)
        }
    }

    // جلب قائمة المحادثات مع المستخدمين (المحادثات المبدوءة مع الـ Admin)
    override fun getUserMessages(): Flow<List<Message>> = flow {
        try {
            val querySnapshot = fireStore.collection("messages")
                .whereEqualTo("receiverId", "zhovdszVmMfIjwgddeCcU94DRiD3") // الـ Admin ID
                .get()
                .await()

            val messages = querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Message::class.java)
            }
            emit(messages)
        } catch (e: Exception) {
            throw Exception("Failed to fetch messages: ${e.message}", e)
        }
    }

    // جلب محادثات مستخدم معين
    override fun getChatWithUser(userId: String): Flow<List<Message>> = flow {
        try {
            val querySnapshot = fireStore.collection("messages")
                .whereIn("senderId", listOf(userId, "zhovdszVmMfIjwgddeCcU94DRiD3"))
                .whereIn("receiverId", listOf(userId, "zhovdszVmMfIjwgddeCcU94DRiD3"))
                .orderBy("timestamp")
                .get()
                .await()

            val messages = querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Message::class.java)
            }
            emit(messages)
        } catch (e: Exception) {
            throw Exception("Failed to fetch chat messages: ${e.message}", e)
        }
    }

    override fun getChatWithCurrentUser(): Flow<List<Message>> = flow {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val adminId = "zhovdszVmMfIjwgddeCcU94DRiD3" // Admin ID

        if (currentUserId != null) {
            try {
                val querySnapshot = fireStore.collection("messages")
                    .whereIn("senderId", listOf(currentUserId, adminId))
                    .whereIn("receiverId", listOf(currentUserId, adminId))
                    .orderBy("timestamp")
                    .get()
                    .await()

                val messages = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Message::class.java)
                }
                emit(messages)
            } catch (e: Exception) {
                throw Exception("Failed to fetch chat messages: ${e.message}", e)
            }
        }
    }


}
