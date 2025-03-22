package com.example.emergen_app.presentation.branch.branchInfo

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorButtonRed
import com.example.emergen_app.utils.getWorkingHours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchInfoScreen(
    navController: NavController
) {
    val viewModel: BranchInfoViewModel = hiltViewModel()
    val branch by viewModel.branch.collectAsState()


    Scaffold(
        topBar = { TopAppBar("My Branch", navController) },
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
                branch?.let { branch ->
                    BranchInfoContent(
                        branch = branch
                    )
                    LogoutButton(userViewModel = viewModel, navController = navController)

                }
            }
        }
    )
}


@Composable
fun BranchInfoContent(
    branch: Branch
) {
    val statusColor = when (branch.typeBranch) {
        "Urgent" -> Color(0xFFFD0008)
        "Medical Emergency" -> Color(0xFF4BD550)
        "Police Emergency" -> Color(0xFF2996FD)
        "Fire Emergency" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = branch.branchName,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = branch.typeBranch,
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 14.sp,
        color = statusColor

    )
    Spacer(modifier = Modifier.height(8.dp))

    BranchInfoCard("Capacity / hour", branch.branchCapacity)
    BranchInfoCard("Mobile", branch.mobileNumber)
    BranchInfoCard("Email", branch.email)
    BranchInfoCard("Password", branch.password)
    BranchInfoWorkingHoursCard(
        label = "Working Hours",
        branch = branch
    )
    BranchInfoCard("Working Days", branch.workDays.joinToString(", "))
    BranchInfoCard("Address", branch.address)
    LocationBranchText("Location", location = branch.addressMaps)
}

@Composable
fun BranchInfoWorkingHoursCard(
    label: String,
    branch: Branch
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp
                )
                .align(Alignment.Start),
            fontSize = 14.sp
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = getWorkingHours(branch.startTime, branch.endTime),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${branch.startTime} : ${branch.endTime}",
                    modifier = Modifier
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
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
                .padding(
                    start = 8.dp
                )
                .align(Alignment.Start),
            fontSize = 14.sp
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
fun BranchInfoCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp
                )
                .align(Alignment.Start),
            fontSize = 14.sp
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
                    .padding(horizontal = 8.dp)
                    .align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun LogoutButton(
    userViewModel: BranchInfoViewModel,
    navController: NavController,
) {
    Button(
        onClick = {
            userViewModel.signOutFromAccount()

            navController.navigate(AppDestination.SignInDestination.route) {
                popUpTo(AppDestination.SignInDestination.route) { inclusive = true }
                launchSingleTop = true
            }

        },
        colors = ButtonDefaults.buttonColors(colorButtonRed),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Logout",
            fontSize = 18.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBranchDetailCard() {
    EmergencyAppTheme {
        BranchInfoCard(
            label = "Branch Name",
            value = "Emergency Center"
        )
    }
}

