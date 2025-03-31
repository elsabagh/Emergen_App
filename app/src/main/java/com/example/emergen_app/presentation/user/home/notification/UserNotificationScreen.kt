package com.example.emergen_app.presentation.user.home.notification

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.presentation.components.TopAppBar

@Composable
fun UserNotificationScreen(
    navController: NavController
) {
    val viewModel: UserNotificationViewModel = hiltViewModel()
    val notifications by viewModel.helpRequests.collectAsState()

    Scaffold(
        topBar = { TopAppBar("Notification", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // استخدام TypeOfRequestText هنا
                LazyColumn {
                    itemsIndexed(notifications) { _, user ->
                        TypeOfRequestText(user = user,
                                typeOfRequest = user.typeOfRequest)
                    }
                }
            }
        }
    )
}


@Composable
fun TypeOfRequestText(user: User?, typeOfRequest: String?) {
    val (icon, color) = when (typeOfRequest) {
        "Fire Emergency" -> Pair(Icons.Filled.NotificationImportant, Color(0xFFF39357))
        "Police Emergency" -> Pair(Icons.Filled.NotificationImportant, Color(0xFF57BFF3))
        "Medical Emergency" -> Pair(Icons.Filled.NotificationImportant, Color(0xFF63D759))
        "Urgent" -> Pair(Icons.Filled.NotificationImportant, Color(0xFFE20303))
        else -> Pair(Icons.Filled.Info, Color.Gray)
    }
    Column {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                )
            }
            Column {
                Text(
                    text = typeOfRequest ?: "No Type Available",
                    color = color,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold

                )
                Text(
                    text = stringResource(R.string.message_notification),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
                Text(
                    text = " Estimated time of arrival: [${user!!.betweenAddress}]",
                    style = MaterialTheme.typography.bodyMedium,
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
fun PreviewTypeOfRequestText() {
    TypeOfRequestText(
        typeOfRequest = "Medical Emergency",
        user = TODO()
    )
}

