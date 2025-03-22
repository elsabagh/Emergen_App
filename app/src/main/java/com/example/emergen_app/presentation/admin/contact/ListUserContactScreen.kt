package com.example.emergen_app.presentation.admin.contact


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.presentation.components.TopAppBar

@Composable
fun ListUserContactScreen(navController: NavController) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    val users = messages.map {
        if (it.senderId != "zhovdszVmMfIjwgddeCcU94DRiD3") it.senderId else it.receiverId
    }.distinct()

    LaunchedEffect(Unit) {
        viewModel.getUserMessages()
    }



    Scaffold(
        topBar = { TopAppBar("Contact", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
            ) {
                LazyColumn {
                    itemsIndexed(users) { index, userId ->
                        val user =
                            messages.find { it.senderId == userId || it.receiverId == userId }

                        user?.let {
                            MessageItem(
                                userId = userId,
                                userName = it.nameSender,
                                userImage = it.imageSender,
                                navController = navController
                            )
                        }
                    }
                }
            }

        }
    )
}

@Composable
fun MessageItem(userId: String, userName: String, userImage: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("chat/$userId") // الانتقال إلى شاشة المحادثة مع المستخدم
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // محاذاة العناصر عمودياً
        ) {
            if (userImage.isNotEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)

                ) {
                    Image(
                        painter = rememberImagePainter(userImage),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = userName)
            }
        }
    }
}