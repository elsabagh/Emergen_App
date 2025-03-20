package com.example.emergen_app.presentation.user.report


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.data.models.User
import com.example.emergen_app.presentation.user.home.AppHeader
import com.example.emergen_app.ui.theme.adminWelcomeCard

@Composable
fun ReportScreen(navController: NavController) {
    val viewModel: ReportViewModel = hiltViewModel()
    val emergencyOptions = listOf("Fire Emergency", "Medical Emergency", "Police Emergency", "All")
    val emergencyTypeOptions = remember { mutableStateOf(emptyList<String>()) }

    val selectedEmergency = remember { mutableStateOf("All") }
    val selectedEmergencyType = remember { mutableStateOf("All") }

    val helpRequests by viewModel.helpRequests.collectAsState()

    val filteredRequests = remember { mutableStateOf<List<User>>(emptyList()) }

    val showResults = remember { mutableStateOf(false) }

    AppHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 106.dp)
    ) {
        EmergencyDropdown(
            title = "Emergency",
            selectedValue = selectedEmergency,
            options = emergencyOptions,
            onSelected = { selected ->
                selectedEmergency.value = selected
                emergencyTypeOptions.value = when (selected) {
                    "Fire Emergency" -> listOf(
                        "All",
                        "House Fire", "Vehicle Fire",
                        "Trapped Cat"
                    )

                    "Medical Emergency" -> listOf(
                        "All",
                        "Obstetric Emergency",
                        "Cardiac Arrest",
                        "Severe Injury"
                    )

                    "Police Emergency" -> listOf(
                        "All",
                        "Armed Robbery",
                        "Violent Assault",
                        "Kidnapping"
                    )

                    else -> listOf("All")
                }
            }
        )

        EmergencyDropdown(
            title = "Emergency Type",
            selectedValue = selectedEmergencyType,
            options = emergencyTypeOptions.value,
            onSelected = { selectedEmergencyType.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                filteredRequests.value = helpRequests.filter {
                    (selectedEmergency.value == "All" || it.typeOfRequest == selectedEmergency.value) &&
                            (selectedEmergencyType.value == "All" || it.typeReason == selectedEmergencyType.value)
                }
                showResults.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(adminWelcomeCard),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Show Results")
        }

        // عرض النتائج عند الضغط على الزر
        if (showResults.value) {
            LazyColumn {
                itemsIndexed(filteredRequests.value) { _, request ->
                    HelpRequestItem(request)
                }
            }
        }
    }
}

@Composable
fun EmergencyDropdown(
    title: String,
    selectedValue: MutableState<String>,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded.value = true }
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selectedValue.value.isNotEmpty()) selectedValue.value else title,
                modifier = Modifier.padding(16.dp)
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    if (expanded.value) {
        AlertDialog(
            onDismissRequest = { expanded.value = false },
            title = { Text(title) },
            text = {
                Column {
                    options.forEach { option ->
                        TextButton(onClick = {
                            selectedValue.value = option
                            onSelected(option)
                            expanded.value = false
                        }) {
                            Text(option)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { expanded.value = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmergencyDropdown() {
    val selectedValue = remember { mutableStateOf("All") }
    val options = listOf("All", "Fire Emergency", "Medical Emergency", "Police Emergency")
    EmergencyDropdown(
        title = "Emergency",
        selectedValue = selectedValue,
        options = options,
        onSelected = { selectedValue.value = it }
    )
}


@Composable
fun HelpRequestItem(request: User) {
    val statusColor = when (request.statusRequest) {
        "Completed" -> Color(0xFFBBE9B7)
        "Team On the Way" -> Color(0xFFFAE0B0)
        "Being Processed" -> Color(0xFFE0E0E0)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = request.typeOfRequest,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = if (request.typeReason.isNotEmpty()) request.typeReason else request.textOther,
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = request.timeOfRequest,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(statusColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = request.statusRequest,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHelpRequestItem() {
    val sampleRequest = User(
        typeOfRequest = "Medical Emergency",
        typeReason = "Cardiac Arrest",
        textOther = "",
        timeOfRequest = "2023-10-01 12:34:56",
        statusRequest = "Being Processed"
    )
    HelpRequestItem(request = sampleRequest)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewReportScreen() {
    val navController = rememberNavController()
    ReportScreen(navController = navController)
}