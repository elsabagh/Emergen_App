package com.example.emergen_app.presentation.admin.contact


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ListUserContactScreen(navController: NavController) {
    val viewModel: ChatViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsState()

    // نحصل على قائمة المستخدمين من الرسائل المسترجعة
    val users = messages.map {
        if (it.senderId != "zhovdszVmMfIjwgddeCcU94DRiD3") it.senderId else it.receiverId
    }.distinct()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Users Chat List", style = MaterialTheme.typography.headlineMedium)

        LazyColumn {
            itemsIndexed(users) { index, userId ->
                MessageItem(
                    userId = userId,
                    navController = navController
                )
            }
        }
    }

    // الحصول على محادثات مع المستخدمين
    LaunchedEffect(Unit) {
        viewModel.getUserMessages()
    }
}

@Composable
fun MessageItem(userId: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("chat/$userId") // الانتقال إلى شاشة المحادثة مع المستخدم
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User: $userId")
        }
    }
}


