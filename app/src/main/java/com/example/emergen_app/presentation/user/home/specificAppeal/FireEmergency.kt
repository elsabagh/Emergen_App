package com.example.emergen_app.presentation.user.home.specificAppeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.user.home.UserHomeViewModel

@Composable
fun FireEmergency(navController: NavController) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var otherText by remember { mutableStateOf("") }
    val userHomeViewModel: UserHomeViewModel = hiltViewModel()
    val user by userHomeViewModel.user.collectAsState()
    val typeOfRequest = "Fire Emergency"

    Scaffold(
        topBar = {
            TopAppBar("Fire Emergency", navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fire_emergency),
                    contentDescription = "Ambulance",
                    modifier = Modifier.size(240.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select type:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                EmergencyOption("House Fire", selectedOption) { selectedOption = it }
                EmergencyOption("Vehicle Fire", selectedOption) { selectedOption = it }
                EmergencyOption("Trapped Cat", selectedOption) { selectedOption = it }

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    text = otherText,
                    onTextChange = { otherText = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                user?.let { validUser ->
                    HelpRequestButton(
                        userHomeViewModel = userHomeViewModel,
                        user = validUser,
                        selectedOption = selectedOption,
                        otherText = otherText,
                        typeOfRequest = typeOfRequest
                    )
                } ?: run {
                    CircularProgressIndicator()
                }

            }
        }
    )
}