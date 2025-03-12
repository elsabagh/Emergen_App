package com.example.emergen_app.presentation.admin.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.data.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(navController: NavController) {

    val accountsViewModel: AccountsViewModel = hiltViewModel()
    val accounts = accountsViewModel.accounts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accepted Accounts") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(accounts.value) { _, account ->
                    AccountItem(account = account)
                }
            }
        }
    }
}

@Composable
fun AccountItem(account: User) {
    Card(
        modifier = Modifier
            .padding(8.dp).padding(horizontal = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            ) {
                // عرض صورة المستخدم
                Image(
                    painter = rememberImagePainter(account.userPhoto),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            // عرض اسم المستخدم
            Text(text = account.userName, style = MaterialTheme.typography.bodySmall)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountsScreen() {
    val sampleAccount = User(
        userName = "John Doe",
        userPhoto = "https://via.placeholder.com/150",
        statusAccount = "accepted",
        role = "user"
    )
    AccountsScreen(navController = rememberNavController())
}
