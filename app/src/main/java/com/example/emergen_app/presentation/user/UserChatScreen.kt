package com.example.emergen_app.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Message
import com.example.emergen_app.presentation.admin.contact.ChatViewModel
import com.example.emergen_app.presentation.components.AppHeader
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserChatScreen(navController: NavController) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    val messageText = remember { mutableStateOf("") }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val currentUserName by viewModel.user.collectAsState()
    val currentUserImage by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
        viewModel.getChatWithCurrentUser()
    }

    AppHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 136.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chat with Admin",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 14.sp,
            modifier = Modifier
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(messages) { index, message ->
                MessageItem(message = message, currentUserId = currentUserId)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                label = { Text("Send message") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    val message = Message(
                        senderId = currentUserId,
                        receiverId = "zhovdszVmMfIjwgddeCcU94DRiD3",
                        content = messageText.value,
                        nameSender = (currentUserName?.userName ?: "").toString(),
                        imageSender = (currentUserImage?.userPhoto ?: "").toString()
                    )
                    viewModel.sendMessage(message)
                    messageText.value = ""
                }
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send Message")
            }
        }
    }
}


@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val sender = if (message.senderId == currentUserId) "You" else "Admin"
    val content = message.content


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.senderId == currentUserId) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Text(
            text = "$sender: $content",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}
