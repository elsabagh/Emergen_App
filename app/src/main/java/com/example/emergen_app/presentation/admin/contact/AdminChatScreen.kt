package com.example.emergen_app.presentation.admin.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.data.models.Message
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorCardIcon

@Composable
fun AdminChatScreen(
    navController: NavController,
    userId: String
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    val messageText = remember { mutableStateOf("") }

    val currentUserId = "zhovdszVmMfIjwgddeCcU94DRiD3" // Admin ID

    LaunchedEffect(userId) {
        viewModel.getChatWithUser(userId)
    }

    val user =
        messages.find { it.senderId == userId || it.receiverId == userId }

    Scaffold(
        topBar = {
            user?.let {
                ChatTopAppBar(
                    userName = it.nameSender,
                    userImage = it.imageSender,
                    navController = navController
                )
            }
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                user?.let {
                    Text(
                        text = "Chat with ${it.nameSender}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            vertical = 16.dp
                        )
                ) {
                    itemsIndexed(messages) { index, message ->
                        MessageItem(
                            message = message,
                            currentUserId = currentUserId
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        label = { Text("Send message") },
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colorCardIcon)
                    ) {
                        IconButton(
                            onClick = {
                                val message = Message(
                                    senderId = currentUserId,
                                    receiverId = userId,
                                    content = messageText.value
                                )
                                viewModel.sendMessage(message)
                                messageText.value = ""
                            },
                        ) {
                            Icon(
                                Icons.Filled.Send, contentDescription = "Send Message",
                                tint = Color.White
                            )
                        }
                    }

                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(userName: String, userImage: String, navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (userImage.isNotEmpty()) {

                    Image(
                        painter = rememberImagePainter(userImage),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = userName)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFF6DD)
        )
    )
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
            .padding(8.dp),
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
                .background(
                    color = cardColor
                )
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                modifier = Modifier
                    .padding(12.dp)
            )
        }

    }
}