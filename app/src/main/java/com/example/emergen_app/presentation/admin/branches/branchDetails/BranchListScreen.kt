package com.example.emergen_app.presentation.admin.branches.branchDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BranchListScreen(navController: NavController, branchName: String) {
    Scaffold(
        topBar = { BranchesTopAppBar("$branchName Branchs", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Details of $branchName branch",
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchesTopAppBar(label: String, navController: NavController) {
    androidx.compose.material3.TopAppBar(
        title = { Text(label) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("add_branch")
            }) {
                Icon(Icons.Default.AddBox, contentDescription = "Add Branch")
            }
        }
    )
}
