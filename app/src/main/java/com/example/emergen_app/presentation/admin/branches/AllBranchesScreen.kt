package com.example.emergen_app.presentation.admin.branches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.ui.theme.colorCardProfile
import com.example.emergen_app.ui.theme.colorFireEmergency
import com.example.emergen_app.ui.theme.colorMedicalEmergency
import com.example.emergen_app.ui.theme.colorPoliceEmergency
import com.example.emergen_app.ui.theme.colorUrgent

@Composable
fun AllBranchesScreen(navController: NavController) {

    Scaffold(
        topBar = { TopAppBar("All branches", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BranchesCard(colorUrgent, "Urgent")
                BranchesCard(colorMedicalEmergency, "Medical Emergency")
                BranchesCard(colorPoliceEmergency, "Police Emergency")
                BranchesCard(colorFireEmergency, "Fire Emergency")

            }
        }
    )
}

@Composable
fun BranchesCard(color: Color, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = value,
                modifier = Modifier.background(color)
                    .fillMaxWidth()
                    .padding(8.dp).padding(vertical = 12.dp)
                    .align(Alignment.Start),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,


                )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
