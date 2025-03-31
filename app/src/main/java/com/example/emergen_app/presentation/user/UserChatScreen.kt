package com.example.emergen_app.presentation.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Message
import com.example.emergen_app.data.models.symbolsList
import com.example.emergen_app.presentation.admin.contact.ChatViewModel
import com.example.emergen_app.presentation.components.AppHeader
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserChatScreen(navController: NavController) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    val messageText = remember { mutableStateOf("") }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val currentUserName by viewModel.user.collectAsState()
    val currentUserImage by viewModel.user.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
        viewModel.getChatWithCurrentUser()
    }

    AppHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 116.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chat with Admin",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 14.sp,
            modifier = Modifier
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(messages) { index, message ->
                MessageItem(message = message, currentUserId = currentUserId)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(symbolsList) { _, symbol ->
                Card(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF6E5))
                        .clickable {
                            val message = Message(
                                senderId = currentUserId,
                                receiverId = "zhovdszVmMfIjwgddeCcU94DRiD3",
                                content = symbol.meaning,
                                imageResId = symbol.imageRes,
                                nameSender = (currentUserName?.userName ?: "").toString(),
                                imageSender = (currentUserImage?.userPhoto ?: "").toString()
                            )
                            viewModel.sendMessage(message)
                        },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,

                        ) {
                        Image(
                            painter = painterResource(id = symbol.imageRes),
                            contentDescription = symbol.meaning,
                            modifier = Modifier
                                .size(144.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }


//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(24.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = messageText.value,
//                onValueChange = { messageText.value = it },
//                label = { Text("Send message") },
//                modifier = Modifier.weight(1f)
//            )
//            Box(
//                modifier = Modifier
//                    .size(36.dp)
//                    .clip(CircleShape)
//                    .background(colorCardIcon)
//            ) {
//                IconButton(
//                    onClick = {
//                        val message = Message(
//                            senderId = currentUserId,
//                            receiverId = "zhovdszVmMfIjwgddeCcU94DRiD3",
//                            content = messageText.value,
//                            nameSender = (currentUserName?.userName ?: "").toString(),
//                            imageSender = (currentUserImage?.userPhoto ?: "").toString()
//                        )
//                        viewModel.sendMessage(message)
//                        messageText.value = ""
//                    }
//                ) {
//                    Icon(
//                        Icons.Filled.Send, contentDescription = "Send Message",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
    }
}


@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val alignment = if (message.senderId == currentUserId) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val cardColor = if (message.senderId == currentUserId) adminWelcomeCard else Color(0xFFE6EAEE)
    val textColor = if (message.senderId == currentUserId) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = if (alignment == Alignment.End) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clip(
                    if (message.senderId == currentUserId) RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    ) else RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(color = cardColor)
                .padding(12.dp)
        ) {
            message.imageResId?.let { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = message.content,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 4.dp)
                )
            }

            if (message.imageResId == null) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMessageItem() {
    val sampleMessage = Message(
        senderId = "sampleUserId",
        receiverId = "sampleReceiverId",
        content = "This is a sample message",
        nameSender = "Sample User",
        imageSender = "sampleImageUrl"
    )
    MessageItem(message = sampleMessage, currentUserId = "sampleUserId")
}