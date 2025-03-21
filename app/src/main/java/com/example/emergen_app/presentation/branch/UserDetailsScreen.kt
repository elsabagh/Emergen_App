package com.example.emergen_app.presentation.branch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import com.example.emergen_app.presentation.admin.userProfile.ImageDialog
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.user.profile.ProfileContent

@Composable
fun UserDetailsScreen(navController: NavController, userId: String) {
    val viewModel: BranchViewModel = hiltViewModel()
    val user by viewModel.userDetails.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        viewModel.getReportById(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar("User profile", navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    user?.let { user ->
                        ProfileContent(user, openDialog, selectedImage)
                        ImageDialog(openDialog, selectedImage)
                    } ?:
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    )
}