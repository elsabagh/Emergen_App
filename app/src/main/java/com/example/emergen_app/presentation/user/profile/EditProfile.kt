package com.example.emergen_app.presentation.user.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.signUp.LoadingOverlay
import com.example.emergen_app.utils.checkIfGpsEnabled
import com.example.emergen_app.utils.updateLocation
import com.google.android.gms.location.LocationServices

@Composable
fun EditProfile(
    navController: NavController,
    userId: String
) {
    val editUserViewModel: EditProfileViewModel = hiltViewModel()
    val state by editUserViewModel.editUserState.collectAsStateWithLifecycle()
    val onPasswordChange: (String) -> Unit = remember { editUserViewModel::onPasswordChange }
    val onConfirmPasswordChange: (String) -> Unit =
        remember { editUserViewModel::onConfirmPasswordChange }
    LaunchedEffect(userId) {
        editUserViewModel.fetchUserData(userId)
    }

    Scaffold(
        topBar = { TopAppBar("Edit", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = state.userName,
                    onValueChange = { editUserViewModel.onUserNameChange(it) },
                    label = { Text("User name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { editUserViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.mobile,
                    onValueChange = { editUserViewModel.onMobileChange(it) },
                    label = { Text("Mobile number") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.governmentName,
                    onValueChange = { editUserViewModel.onGovernmentChange(it) },
                    label = { Text("Government name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.area,
                    onValueChange = { editUserViewModel.onAreaChange(it) },
                    label = { Text("Area") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.plotNumber,
                    onValueChange = { editUserViewModel.onPlotNumberChange(it) },
                    label = { Text("Plot number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.streetName,
                    onValueChange = { editUserViewModel.onStreetNameChange(it) },
                    label = { Text("Street name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.buildNumber,
                    onValueChange = { editUserViewModel.onBuildNumberChange(it) },
                    label = { Text("Build number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.floorNumber,
                    onValueChange = { editUserViewModel.onFloorNumberChange(it) },
                    label = { Text("Floor number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.apartmentNumber,
                    onValueChange = { editUserViewModel.onApartmentNumberChange(it) },
                    label = { Text("Apartment number") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Address Map
                LocationButton(context = LocalContext.current, state, editUserViewModel)

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { editUserViewModel.onPasswordChange(it) },
                    label = { Text("Old Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = { editUserViewModel.onNewPasswordChange(it) },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        editUserViewModel.changePassword()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(" Change password")
                }

                Button(
                    onClick = {
                        editUserViewModel.updateUserProfile(navController)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }


                if (state.isLoading) {
                    LoadingOverlay()
                }

            }
        }
    )
}

@Composable
fun LocationButton(context: Context, uiState: EditUserState, viewModel: EditProfileViewModel) {

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = uiState.addressMaps,
            onValueChange = viewModel::onAddressMapsChange,
            label = { Text("Address Location (Google Maps)") },
            modifier = Modifier
        )

        IconButton(onClick = {
            Log.d("LocationDebug", "Location button clicked")

            val isGpsEnabled = checkIfGpsEnabled(context)
            if (!isGpsEnabled) {
                Log.e("LocationDebug", "GPS is disabled")
                Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show()
                return@IconButton
            }

            Log.d("LocationDebug", "GPS is enabled, fetching location...")

            updateLocation(fusedLocationClient, context) { location ->
                Log.d("LocationDebug", "Location received: $location")
                viewModel.onAddressMapsChange(location)
            }
        }) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Get Location",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }

    }
}