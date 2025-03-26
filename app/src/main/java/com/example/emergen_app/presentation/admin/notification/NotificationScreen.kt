package com.example.emergen_app.presentation.admin.notification


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.data.models.User
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.ui.theme.adminWelcomeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val notifications = notificationViewModel.notifications.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                "Notifications", navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(notifications.value) { _, notification ->
                    NotificationItem(notification = notification, navController = navController)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: User, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("user_profile/${notification.userId}/true")
            },
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(

            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationImportant,
                    contentDescription = null,
                    tint = adminWelcomeCard,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "New Account", style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Review the account and you can activate it by accepting or rejecting the account",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    val user = User(
        userName = "John Doe",
        userPhoto = "https://via.placeholder.com/150",
        statusAccount = "Active",
        role = "Admin"
    )
    NotificationItem(
        notification = user,
        navController = rememberNavController()
    )
}