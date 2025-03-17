package com.example.emergen_app.presentation.user.home.specificAppeal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.emergen_app.presentation.components.TopAppBar

@Composable
fun PoliceEmergency(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar("Police Emergency", navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    )
}