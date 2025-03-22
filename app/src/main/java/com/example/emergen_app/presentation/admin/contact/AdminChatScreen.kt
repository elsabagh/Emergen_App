package com.example.emergen_app.presentation.admin.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Message

@Composable
fun AdminChatScreen(navController: NavController, userId: String) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    val messageText = remember { mutableStateOf("") }

    // الحصول على الـ userId للمستخدم الحالي
    val currentUserId = "zhovdszVmMfIjwgddeCcU94DRiD3" // Admin ID

    // الحصول على المحادثات مع المستخدم عند بداية الشاشة
    LaunchedEffect(userId) {
        viewModel.getChatWithUser(userId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Chat with User $userId", style = MaterialTheme.typography.headlineMedium)

        // عرض الرسائل باستخدام LazyColumn
        LazyColumn(
            modifier = Modifier.weight(1f) // تأكد من أن الرسائل تأخذ المساحة المتبقية
        ) {
            itemsIndexed(messages) { index, message ->
                MessageItem(message = message, currentUserId = currentUserId) // عرض الرسائل مع المحاذاة
            }
        }

        // تثبيت الـ TextField و Button في أسفل الشاشة
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            TextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                label = { Text("Type a message") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val message = Message(
                        senderId = currentUserId, // Admin ID
                        receiverId = userId,
                        content = messageText.value
                    )
                    viewModel.sendMessage(message) // إرسال الرسالة
                    messageText.value = ""  // Clear the text field
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Send")
            }
        }
    }
}


@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val alignment = if (message.senderId == currentUserId) {
        Alignment.End // رسائل المستخدم تكون على اليمين
    } else {
        Alignment.Start // رسائل الـ Admin تكون على اليسار
    }

    // عرض الرسالة مع محاذاة حسب المرسل
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (alignment == Alignment.End) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}
