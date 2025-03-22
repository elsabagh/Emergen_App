package com.example.emergen_app.presentation.admin.branches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.colorFireEmergency
import com.example.emergen_app.ui.theme.colorMedicalEmergency
import com.example.emergen_app.ui.theme.colorPoliceEmergency
import com.example.emergen_app.ui.theme.colorUrgent

@Composable
fun AllBranchesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar("All branches", navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BranchesCard(navController, colorUrgent, "Urgent")
                BranchesCard(navController, colorMedicalEmergency, "Medical Emergency")
                BranchesCard(navController, colorPoliceEmergency, "Police Emergency")
                BranchesCard(navController, colorFireEmergency, "Fire Emergency")
            }
        }
    )
}

@Composable
fun BranchesCard(navController: NavController, color: Color, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("branch_list/$value")
                }
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .background(color)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(vertical = 12.dp)
                    .align(Alignment.Start),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAllBranchesScreen() {
    EmergencyAppTheme {
        AllBranchesScreen(navController = rememberNavController())
    }
}
