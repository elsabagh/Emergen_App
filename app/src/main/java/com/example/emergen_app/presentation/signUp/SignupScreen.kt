package com.example.emergen_app.presentation.signUp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.R
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.utils.checkIfGpsEnabled
import com.example.emergen_app.utils.updateLocation
import com.google.android.gms.location.LocationServices


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isAccountCreated by viewModel.isAccountCreated.collectAsStateWithLifecycle()

    var userPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var idFrontUri by remember { mutableStateOf<Uri?>(null) }
    var idBackUri by remember { mutableStateOf<Uri?>(null) }

    val userPhotoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            userPhotoUri = uri
        }

    val idFrontPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            idFrontUri = uri
        }

    val idBackPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            idBackUri = uri
        }


    LaunchedEffect(isAccountCreated) {
        if (isAccountCreated) {
            SnackBarManager.showMessage(R.string.created_account)
            viewModel.resetIsAccountCreated()

            navController.popBackStack(AppDestination.SignInDestination.route, inclusive = false)
            navController.navigate(AppDestination.SignInDestination.route) {
                popUpTo(AppDestination.SignUpDestination.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar("Sign up", navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(bottom = 24.dp),
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserPhotoSection(userPhotoUri, userPhotoPicker)

                Spacer(modifier = Modifier.height(16.dp))

                UserDetailsSection(uiState, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                AddressSection(uiState, viewModel)


                Spacer(modifier = Modifier.height(16.dp))

                UploadIDSection(
                    idFrontUri = idFrontUri,
                    idBackUri = idBackUri,
                    idFrontPicker = idFrontPicker,
                    idBackPicker = idBackPicker,
                    onDeleteFront = { idFrontUri = null },
                    onDeleteBack = { idBackUri = null }
                )


                Spacer(modifier = Modifier.height(16.dp))

                PasswordSection(uiState, viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                SignUpButton(uiState.isLoading) {
                    viewModel.createAccount(idFrontUri, idBackUri, userPhotoUri)
                }
            }

            if (uiState.isLoading) {
                LoadingOverlay()
            }
        }
    }

}


@Composable
fun UserPhotoSection(
    userPhotoUri: Uri?,
    userPhotoPicker: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp)
            .clickable { userPhotoPicker.launch("image/*") },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "User photo",
            modifier = Modifier,
            fontSize = 16.sp
        )
        if (userPhotoUri != null) {
            Image(
                painter = rememberImagePainter(userPhotoUri),
                contentDescription = "User Photo",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.add_user_photo),
                contentDescription = "Upload Photo",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun UserDetailsSection(uiState: SignUpState, viewModel: SignUpViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = uiState.userName,
            onValueChange = viewModel::onUserNameChange,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.mobile,
            onValueChange = viewModel::onMobileChange,
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()

        )
    }
}

@Composable
fun AddressSection(uiState: SignUpState, viewModel: SignUpViewModel) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Address", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = uiState.governmentName,
            onValueChange = viewModel::onGovernmentChange,
            label = { Text("Governorate Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.area,
            onValueChange = viewModel::onAreaChange,
            label = { Text("Area") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.plotNumber,
            onValueChange = viewModel::onPlotNumberChange,
            label = { Text("Plot Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.streetName,
            onValueChange = viewModel::onStreetNameChange,
            label = { Text("Street Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.buildNumber,
            onValueChange = viewModel::onBuildNumberChange,
            label = { Text("Building Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.floorNumber,
            onValueChange = viewModel::onFloorNumberChange,
            label = { Text("Floor Number (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.apartmentNumber,
            onValueChange = viewModel::onApartmentNumberChange,
            label = { Text("Apartment Number (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        LocationButton(context = LocalContext.current, uiState, viewModel)

    }
}

@Composable
fun LocationButton(context: Context, uiState: SignUpState, viewModel: SignUpViewModel) {
    var latitude = uiState.latitude
    var longitude = uiState.longitude

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = latitude,
            onValueChange = {
                viewModel::onLatitudeChange
            },
            label = { Text("Latitude") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = {
                viewModel::onLongitudeChange
            },
            label = { Text("Longitude") },
            modifier = Modifier.weight(1f)
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
                val (lat, lon) = location.split(",")
                latitude = lat.trim()
                longitude = lon.trim()

                viewModel.onLatitudeChange(latitude)
                viewModel.onLongitudeChange(longitude)
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

@Composable
fun UploadIDSection(
    idFrontUri: Uri?,
    idBackUri: Uri?,
    idFrontPicker: ManagedActivityResultLauncher<String, Uri?>,
    idBackPicker: ManagedActivityResultLauncher<String, Uri?>,
    onDeleteFront: () -> Unit,
    onDeleteBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Upload ID", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(120.dp)
                        .border(
                            2.dp,
                            Color.Gray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Front",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    idFrontUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "ID Front",
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(120.dp)
                            .clickable { idFrontPicker.launch("image/*") },
                        contentAlignment = Alignment.Center,
                    ) {
                        Button(
                            onClick = { idFrontPicker.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(adminWelcomeCard),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Upload")
                        }
                    }

                    if (idFrontUri != null) {
                        IconButton(
                            onClick = onDeleteFront,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(120.dp)
                        .border(
                            2.dp,
                            Color.Gray,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    idBackUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "ID Back",
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(120.dp)
                            .clickable { idBackPicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { idBackPicker.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(adminWelcomeCard),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Upload")
                        }
                    }

                    if (idBackUri != null) {
                        IconButton(
                            onClick = onDeleteBack,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PasswordSection(uiState: SignUpState, viewModel: SignUpViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()

        )
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()

        )
    }
}

@Composable
fun SignUpButton(isLoading: Boolean, onClick: () -> Unit) {
    if (!isLoading) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(adminWelcomeCard),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Sign Up")
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    SignupScreen(navController = rememberNavController())
}
