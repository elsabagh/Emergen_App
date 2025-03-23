package com.example.emergen_app.presentation.user.home.specificAppeal

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.presentation.components.TopAppBar
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.presentation.user.home.UserHomeViewModel
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.utils.checkIfGpsEnabled
import com.example.emergen_app.utils.fetchLocation
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedicalEmergency(navController: NavController) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var otherText by remember { mutableStateOf("") }
    val userHomeViewModel: UserHomeViewModel = hiltViewModel()
    val user by userHomeViewModel.user.collectAsState()
    val typeOfRequest = "Medical Emergency"

    Scaffold(
        topBar = { TopAppBar("Medical Emergency", navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // صورة سيارة الإسعاف
                Image(
                    painter = painterResource(id = R.drawable.medical_emergency), // استبدل بالصور المناسبة
                    contentDescription = "Ambulance",
                    modifier = Modifier.size(220.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select type:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                EmergencyOption("Obstetric Emergency", selectedOption) { selectedOption = it }
                EmergencyOption("Cardiac Arrest", selectedOption) { selectedOption = it }
                EmergencyOption("Severe Injury", selectedOption) { selectedOption = it }

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

@Composable
fun EmergencyOption(text: String, selectedOption: String?, onSelect: (String) -> Unit) {
    val isSelected = selectedOption == text
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                2.dp,
                if (isSelected) adminWelcomeCard else Color.Gray,
                RoundedCornerShape(8.dp)
            )
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { onSelect(text) }
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyOptionPreview() {
    EmergencyOption(text = "Cardiac Arrest", selectedOption = "Cardiac Arrest", onSelect = {})
}


@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }

    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
        modifier = focusModifier,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .border(
                        width = 2.dp,
                        color = if (isFocused) adminWelcomeCard else Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                if (text.isEmpty()) Text("Other..", color = Color.Gray)
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        text = "",
        onTextChange = {}
    )
}

@Composable
fun HelpRequestButton(
    userHomeViewModel: UserHomeViewModel,
    user: User,
    selectedOption: String?,
    otherText: String,
    typeOfRequest: String
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // متغير لتخزين الموقع
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var currentTime by remember { mutableStateOf<String>("") }

    Button(
        onClick = {
            val isGpsEnabled = checkIfGpsEnabled(context)
            if (!isGpsEnabled) {
                Log.e("LocationDebug", "GPS is disabled")
                SnackBarManager.showMessage(R.string.please_enable_gps)
            } else {
                fetchLocation(fusedLocationClient, context) { latitude, longitude ->
                    currentLocation = Pair(latitude, longitude)

                    val currentDate = Date()
                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    currentTime = dateFormat.format(currentDate)

                    // استدعاء دالة `createHelpRequest`
                    userHomeViewModel.createUrgentHelpRequest(
                        user.copy(
                            timeOfRequest = currentTime,
                            typeOfRequest = typeOfRequest,
                            textOther = otherText,
                            typeReason = selectedOption ?: "",
                            addressMaps = "${currentLocation?.first},${currentLocation?.second}",
                            statusRequest = "Being Processed"
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(adminWelcomeCard),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "Help Request",
            modifier = Modifier,
            fontSize = 18.sp
        )
    }
}

