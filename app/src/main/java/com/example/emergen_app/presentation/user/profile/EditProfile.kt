package com.example.emergen_app.presentation.user.profile

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
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
    val openDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val idFrontUri = remember { mutableStateOf<Uri?>(null) }
    val idBackUri = remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> imageUri.value = uri }
    )

    val pickIdFrontLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> idFrontUri.value = uri }
    )

    val pickIdBackLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> idBackUri.value = uri }
    )

    LaunchedEffect(userId) {
        editUserViewModel.fetchUserData(userId)
    }
    val openEmailDialog = remember { mutableStateOf(false) }
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

                EditUserImage(
                    state = state,
                    imageUri = imageUri,
                    pickImageLauncher = pickImageLauncher
                )

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
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { openEmailDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Email")
                }

                if (openEmailDialog.value) {
                    ChangeEmailDialog(
                        onDismiss = { openEmailDialog.value = false },
                        onChangeEmail = { newEmail, currentPassword ->
                            editUserViewModel.updateUserEmail(
                                currentPassword = currentPassword,
                                newEmail = newEmail,
                                navController = navController, // ✅ تم تمرير `navController`
                                onSuccess = {
                                    Toast.makeText(context, "Email updated successfully!", Toast.LENGTH_SHORT).show()
                                    openEmailDialog.value = false
                                },
                                onFailure = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }



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
                LocationButton(context = LocalContext.current, state, editUserViewModel)

                Button(
                    onClick = { openDialog.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change password")
                }

                if (openDialog.value) {
                    ChangePasswordDialog(
                        onDismiss = { openDialog.value = false },
                        onChangePassword = { oldPassword, newPassword ->
                            editUserViewModel.onPasswordChange(oldPassword)
                            editUserViewModel.onNewPasswordChange(newPassword)
                            editUserViewModel.changePassword()
                            openDialog.value = false
                        }
                    )
                }

                EditIdImage(
                    state = state,
                    idFrontUri = idFrontUri,
                    idBackUri = idBackUri,
                    pickIdFrontLauncher = pickIdFrontLauncher,
                    pickIdBackLauncher = pickIdBackLauncher
                )

                Button(
                    onClick = {
                        editUserViewModel.updateUserProfile(
                            navController,
                            imageUri.value,
                            idFrontUri.value,
                            idBackUri.value
                        )
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
fun ChangeEmailDialog(
    onDismiss: () -> Unit,
    onChangeEmail: (String, String) -> Unit
) {
    val newEmail = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Email") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newEmail.value,
                    onValueChange = { newEmail.value = it },
                    label = { Text("New Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = currentPassword.value,
                    onValueChange = { currentPassword.value = it },
                    label = { Text("Current Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangeEmail(newEmail.value, currentPassword.value)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun EditIdImage(
    state: EditUserState,
    idFrontUri: MutableState<Uri?>,
    idBackUri: MutableState<Uri?>,
    pickIdFrontLauncher: ManagedActivityResultLauncher<String, Uri?>,
    pickIdBackLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Front",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier
            )

            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(120.dp)
                    .border(
                        2.dp,
                        Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {

                val idFrontToShow = idFrontUri.value ?: state.idFront
                idFrontToShow?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "ID Front",
                        modifier = Modifier.size(100.dp)
                    )
                }
                IconButton(
                    onClick = { pickIdFrontLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White, shape = RoundedCornerShape(
                                8.dp
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",

                        )
                }
            }
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Back",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(120.dp)
                    .border(
                        2.dp,
                        Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {

                val idBackToShow = idBackUri.value ?: state.idBack
                idBackToShow?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "ID Back",
                        modifier = Modifier.size(100.dp)
                    )
                }


                IconButton(
                    onClick = { pickIdBackLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White, shape = RoundedCornerShape(
                                8.dp
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }

            }
        }
    }
}

@Composable
fun EditUserImage(
    state: EditUserState,
    imageUri: MutableState<Uri?>,
    pickImageLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(100.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(
                    shape = RoundedCornerShape(
                        8.dp
                    )
                )
        ) {
            val photoToShow = imageUri.value ?: state.userPhoto
            photoToShow?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .size(80.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = RoundedCornerShape(
                                8.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier
                    .size(80.dp),
            ) {
                IconButton(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White, shape = RoundedCornerShape(
                                8.dp
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
            }

        }
    }
}


@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    val oldPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = oldPassword.value,
                    onValueChange = { oldPassword.value = it },
                    label = { Text("Old Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangePassword(oldPassword.value, newPassword.value)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LocationButton(context: Context, uiState: EditUserState, viewModel: EditProfileViewModel) {

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
            onValueChange = viewModel::onLatitudeChange,
            label = { Text("Latitude") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = viewModel::onLongitudeChange,
            label = { Text("Longitude") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            val isGpsEnabled = checkIfGpsEnabled(context)
            if (!isGpsEnabled) {
                Log.e("LocationDebug", "GPS is disabled")
                Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show()
                return@IconButton
            }

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

@Preview(showBackground = true)
@Composable
fun PreviewChangePasswordDialog() {
    ChangePasswordDialog(
        onDismiss = { /* Do nothing on dismiss */ },
        onChangePassword = { old, new ->
            println("Old Password: $old, New Password: $new")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChangeEmailDialog() {
    ChangeEmailDialog(
        onDismiss = { /* Do nothing on dismiss */ },
        onChangeEmail = { newEmail, currentPassword ->
            println("New Email: $newEmail, Current Password: $currentPassword")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEditIdImage() {
    val state = EditUserState(
        idFront = "https://example.com/id_front.jpg",
        idBack = "https://example.com/id_back.jpg"
    )
    val idFrontUri = remember { mutableStateOf<Uri?>(null) }
    val idBackUri = remember { mutableStateOf<Uri?>(null) }
    val pickIdFrontLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> idFrontUri.value = uri }
    )
    val pickIdBackLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> idBackUri.value = uri }
    )
    EditIdImage(
        state = state,
        idFrontUri = idFrontUri,
        idBackUri = idBackUri,
        pickIdFrontLauncher = pickIdFrontLauncher,
        pickIdBackLauncher = pickIdBackLauncher
    )
}
