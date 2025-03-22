package com.example.emergen_app.presentation.admin.branches.branchList.branchDetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.presentation.branch.branchInfo.BranchInfoWorkingHoursCard
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.adminWelcomeCard

@Composable
fun BranchDetailsScreen(navController: NavController, branchId: String) {
    val viewModel: BranchDetailsViewModel = hiltViewModel()
    val branch by viewModel.branch.collectAsState()


    // تحميل بيانات الفرع عند فتح الشاشة
    LaunchedEffect(branchId) {
        viewModel.fetchBranchDetails(branchId)
    }

    Scaffold(
        topBar = { BranchesDetailsTopAppBar(branch, navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (branch.branchName.isNotEmpty()) {
                    // ✅ استدعاء `BranchContent` وتمرير بيانات الفرع
                    BranchContent(branch)
                } else {
                    // ✅ إظهار رسالة تحميل عند عدم توفر البيانات
                    CircularProgressIndicator()
                }
            }
        }
    )
}


@Composable
fun BranchContent(
    branch: Branch
) {
    val statusColor = when (branch.typeBranch) {
        "Urgent" -> Color(0xFFFD0008)
        "Medical Emergency" -> Color(0xFF4BD550)
        "Police Emergency" -> Color(0xFF2996FD)
        "Fire Emergency" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
    Text(
        text = branch.branchName,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 18.sp
    )
    Text(
        text = branch.typeBranch,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 14.sp,
        color = statusColor

    )

    BranchDetailCard("Capacity / hour", branch.branchCapacity)
    BranchDetailCard("Mobile", branch.mobileNumber)
    BranchDetailCard("Email", branch.email)
    BranchDetailCard("Password", branch.password)
    BranchInfoWorkingHoursCard(
        label = "Working Hours",
        branch = branch
    )
    BranchDetailCard("Working Hours", branch.workDays.joinToString(", "))
    BranchDetailCard("Address", branch.address)
    LocationBranchText("Location", location = branch.addressMaps)
}

@Composable
fun LocationBranchText(label: String, location: String) {
    var locationText by remember { mutableStateOf(location) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "location Link",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        // تحويل الإحداثيات إلى رابط Google Maps بشكل صحيح
                        val uri = "geo:$locationText?q=$locationText"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    },
                color = adminWelcomeCard
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun BranchDetailCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBranchDetailCard() {
    EmergencyAppTheme {
        BranchDetailCard(
            label = "Branch Name",
            value = "Emergency Center"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchesDetailsTopAppBar(branch: Branch, navController: NavController) {
    TopAppBar(
        title = { Text(branch.branchName) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("edit_branch/${branch.userId}") // ✅ التنقل إلى صفحة التعديل مع تمرير الـ ID
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Branch")
            }
        }
    )
}


