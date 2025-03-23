package com.example.emergen_app.presentation.admin.branches.addBranch

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.R
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorCardDay
import com.example.emergen_app.ui.theme.colorTextDay
import com.example.emergen_app.utils.checkIfGpsEnabled
import com.example.emergen_app.utils.updateLocation
import com.google.android.gms.location.LocationServices
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBranchScreen(navController: NavController, branchName: String) {
    val addBranchViewModel: AddBranchViewModel = hiltViewModel()
    val state by addBranchViewModel.state.collectAsStateWithLifecycle()

    // تعيين typeBranch عند فتح الشاشة
    LaunchedEffect(branchName) {
        addBranchViewModel.onTypeBranchChange(branchName)
    }

    Scaffold(
        topBar = { TopAppBar("Add Branch", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Branch Name
                OutlinedTextField(
                    value = state.branchName,
                    onValueChange = { addBranchViewModel.onBranchNameChange(it) },
                    label = { Text("Branch name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Branch Capacity
                OutlinedTextField(
                    value = state.branchCapacity,
                    onValueChange = { addBranchViewModel.onBranchCapacityChange(it) },
                    label = { Text("Branch capacity / hour") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { addBranchViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Mobile Number
                OutlinedTextField(
                    value = state.mobileNumber,
                    onValueChange = { addBranchViewModel.onMobileNumberChange(it) },
                    label = { Text("Mobile number") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Address
                OutlinedTextField(
                    value = state.address,
                    onValueChange = { addBranchViewModel.onAddressChange(it) },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                LocationButton(
                    context = navController.context,
                    uiState = state,
                    viewModel = addBranchViewModel
                )

                // Password
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { addBranchViewModel.onPasswordChange(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                )

                WorkDaysSection(state.workDays, addBranchViewModel)

                WorkTimeSection(state.startTime, state.endTime, addBranchViewModel)

                Button(
                    onClick = {
                        if (state.branchName.isNotEmpty() && state.email.isNotEmpty()) {
                            addBranchViewModel.createBranchAccount(navController)
                        } else {
                            SnackBarManager.showMessage(R.string.fill_all_fields)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(adminWelcomeCard),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add Branch")
                }
            }
        }
    )
}

@Composable
fun LocationButton(context: Context, uiState: AddBranchState, viewModel: AddBranchViewModel) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = latitude,
            onValueChange = { newValue ->
                latitude = newValue
                // تحديث الـ uiState مع الإحداثيات الجديدة
                viewModel.onLatitudeChange(newValue)
            },
            label = { Text("Latitude") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = { newValue ->
                longitude = newValue
                // تحديث الـ uiState مع الإحداثيات الجديدة
                viewModel.onLongitudeChange(newValue)
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

                // تحديث الـ viewModel مع الإحداثيات الجديدة
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkDaysSection(workDays: List<String>, addBranchViewModel: AddBranchViewModel) {
    val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var selectedDays by remember { mutableStateOf(workDays) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        days.forEach { day ->
            val isSelected = selectedDays.contains(day)
            Card(
                onClick = {
                    selectedDays = if (isSelected) {
                        selectedDays - day
                    } else {
                        selectedDays + day
                    }
                    addBranchViewModel.onWorkDaysChange(selectedDays)
                },
                modifier = Modifier
            ) {
                Text(
                    day,
                    modifier = Modifier
                        .background(
                            if (isSelected) adminWelcomeCard
                            else colorCardDay
                        )
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 8.dp),
                    color = if (isSelected) Color.White else colorTextDay
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkTimeSection(startTime: String, endTime: String, addBranchViewModel: AddBranchViewModel) {
    var isStartTimeDialogVisible by remember { mutableStateOf(false) }
    var isEndTimeDialogVisible by remember { mutableStateOf(false) }

    // Start Time
    OutlinedTextField(
        value = startTime,
        onValueChange = {},
        label = { Text("Start time") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { isStartTimeDialogVisible = true }) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Select time")
            }
        }
    )

    // End Time
    OutlinedTextField(
        value = endTime,
        onValueChange = {},
        label = { Text("End time") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { isEndTimeDialogVisible = true }) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Select time")
            }
        }
    )

    if (isStartTimeDialogVisible) {
        TimeSelectionDialog(
            onConfirm = { hour, minute ->
                val selectedTime = "$hour:${minute.toString().padStart(2, '0')}"
                addBranchViewModel.onStartTimeChange(selectedTime)
                isStartTimeDialogVisible = false
            },
            onDismiss = { isStartTimeDialogVisible = false }
        )
    }

    if (isEndTimeDialogVisible) {
        TimeSelectionDialog(
            onConfirm = { hour, minute ->
                val selectedTime = "$hour:${minute.toString().padStart(2, '0')}"
                addBranchViewModel.onEndTimeChange(selectedTime)
                isEndTimeDialogVisible = false
            },
            onDismiss = { isEndTimeDialogVisible = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionDialog(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )
    TimePickerDialog(
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(
                timePickerState.hour,
                timePickerState.minute
            )
        },
        modifier = modifier
    ) {
        TimePicker(
            state = timePickerState
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dismissText = stringResource(R.string.cancel)
    val confirmText = stringResource(R.string.confirm)
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        text = { content() },
        modifier = modifier
    )
}


@Preview
@Composable
private fun TimeSelectionDialogPreview() {
    EmergencyAppTheme {
        TimeSelectionDialog(
            onConfirm = { _, _ -> },
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun TimePickerDialogPreview() {
    EmergencyAppTheme {
        TimePickerDialog(
            onDismiss = {},
            onConfirm = {}
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddBranchScreen() {
    EmergencyAppTheme {
        AddBranchScreen(
            navController = rememberNavController(),
            branchName = "ugert"
        )
    }
}
