package com.example.emergen_app.presentation.admin.branches.branchList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.ui.theme.EmergencyAppTheme

@Composable
fun BranchListScreen(navController: NavController, branchName: String) {
    val viewModel: BranchListViewModel = hiltViewModel()
    val branches by viewModel.branches.collectAsState()

    LaunchedEffect(branchName) {
        viewModel.fetchBranches(branchName)
    }

    Scaffold(
        topBar = { BranchesTopAppBar(branchName, navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(branches) { branch ->
                        BranchItem(
                            branch,
                            onClick = {
                                navController.navigate("branch_details/${branch.userId}")
                            },
                            onEditClick = {
                                navController.navigate("edit_branch/${branch.userId}") // ✅ التنقل إلى صفحة التعديل مع تمرير ID
                            },
                            onDeleteClick = { viewModel.deleteBranch(branch.userId) }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun BranchItem(
    branch: Branch,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = "Branch: ${branch.branchName}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = branch.mobileNumber, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEditClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchesTopAppBar(label: String, navController: NavController) {
    TopAppBar(
        title = { Text(label) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("add_branch/$label")
            }) {
                Icon(Icons.Default.AddBox, contentDescription = "Add Branch")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFF6DD)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBranchItem() {
    EmergencyAppTheme {
        BranchItem(
            branch = Branch(
                branchName = "Emergency Center",
                mobileNumber = "+20123456789"
            ),
            onEditClick = { },
            onDeleteClick = { },
            onClick = { }
        )
    }
}