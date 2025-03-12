package com.example.emergen_app.presentation.signUp

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.R
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager

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

            navController.navigate(AppDestination.SignInDestination.route) {
                popUpTo(AppDestination.SignUpDestination.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

            UploadIDSection(idFrontUri, idBackUri, idFrontPicker, idBackPicker)

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

@Composable
fun UserPhotoSection(userPhotoUri: Uri?, userPhotoPicker: ManagedActivityResultLauncher<String, Uri?>) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable { userPhotoPicker.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        if (userPhotoUri != null) {
            Image(
                painter = rememberImagePainter(userPhotoUri),
                contentDescription = "User Photo",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                contentDescription = "Upload Photo",
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
fun UserDetailsSection(uiState: SignUpState, viewModel: SignUpViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = uiState.userName,
            onValueChange = viewModel::onUserNameChange,
            label = { Text("Full Name") })
        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") })
        OutlinedTextField(
            value = uiState.mobile,
            onValueChange = viewModel::onMobileChange,
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
            label = { Text("Governorate Name") })
        OutlinedTextField(
            value = uiState.area,
            onValueChange = viewModel::onAreaChange,
            label = { Text("Area") })
        OutlinedTextField(
            value = uiState.plotNumber,
            onValueChange = viewModel::onPlotNumberChange,
            label = { Text("Plot Number") })
        OutlinedTextField(
            value = uiState.streetName,
            onValueChange = viewModel::onStreetNameChange,
            label = { Text("Street Name") })
        OutlinedTextField(
            value = uiState.buildNumber,
            onValueChange = viewModel::onBuildNumberChange,
            label = { Text("Building Number") })
        OutlinedTextField(
            value = uiState.floorNumber,
            onValueChange = viewModel::onFloorNumberChange,
            label = { Text("Floor Number (Optional)") })
        OutlinedTextField(
            value = uiState.apartmentNumber,
            onValueChange = viewModel::onApartmentNumberChange,
            label = { Text("Apartment Number (Optional)") })
        OutlinedTextField(
            value = uiState.addressMaps,
            onValueChange = viewModel::onAddressMapsChange,
            label = { Text("Address Location (Google Maps)") })
    }
}

@Composable
fun UploadIDSection(
    idFrontUri: Uri?,
    idBackUri: Uri?,
    idFrontPicker: ManagedActivityResultLauncher<String, Uri?>,
    idBackPicker: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Upload ID", style = MaterialTheme.typography.bodySmall)
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                idFrontUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "ID Front",
                        modifier = Modifier.size(80.dp)
                    )
                } ?: Icon(
                    painter = painterResource(id = R.drawable.baseline_file_upload_24),
                    contentDescription = "Upload Front ID",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { idFrontPicker.launch("image/*") }
                )
                Button(onClick = { idFrontPicker.launch("image/*") }) {
                    Text("Front")
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                idBackUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "ID Back",
                        modifier = Modifier.size(80.dp)
                    )
                } ?: Icon(
                    painter = painterResource(id = R.drawable.baseline_file_upload_24),
                    contentDescription = "Upload Back ID",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { idBackPicker.launch("image/*") }
                )
                Button(onClick = { idBackPicker.launch("image/*") }) {
                    Text("Back")
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}

@Composable
fun SignUpButton(isLoading: Boolean, onClick: () -> Unit) {
    if (!isLoading) {
        Button(onClick = onClick) {
            Text("Sign Up")
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent background
            .clickable(enabled = false) {}, // Disable interactions
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
