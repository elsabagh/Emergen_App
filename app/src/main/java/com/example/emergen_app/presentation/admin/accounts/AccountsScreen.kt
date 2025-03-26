package com.example.emergen_app.presentation.admin.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.data.models.User
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.ui.theme.colorButtonRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(navController: NavController) {

    val accountsViewModel: AccountsViewModel = hiltViewModel()
    val accounts = accountsViewModel.accounts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                "All Accounts", navController
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
                    AccountItem(
                        account = account,
                        navController = navController,
                        onDeleteClick = { userId ->
                            accountsViewModel.deleteAccount(userId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AccountItem(
    account: User, navController: NavController,
    onDeleteClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate("user_profile/${account.userId}/false")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)

            ) {
                Image(
                    painter = rememberImagePainter(account.userPhoto),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier) {
                Text(text = account.userName, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onDeleteClick(account.userId)
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Edit Account",
                    tint = colorButtonRed
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountsScreen() {
    val sampleAccounts = listOf(
        User(
            userId = "1",
            userName = "John Doe",
            userPhoto = "https://via.placeholder.com/150",
            statusAccount = "accepted",
            role = "user"
        ),
        User(
            userId = "2",
            userName = "Jane Smith",
            userPhoto = "https://via.placeholder.com/150",
            statusAccount = "pending",
            role = "admin"
        )
    )
    val navController = rememberNavController()
    Column {
        sampleAccounts.forEach { account ->
            AccountItem(
                account = account,
                navController = navController,
                onDeleteClick = { })
        }
    }
}