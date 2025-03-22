package com.example.emergen_app.presentation.user.home

import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.AppHeader
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.colorButtonRed
import com.example.emergen_app.utils.checkIfGpsEnabled
import com.example.emergen_app.utils.fetchLocation
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserHomeScreen(
    navController: NavController,
) {
    val userHomeViewModel: UserHomeViewModel = hiltViewModel()
    val user by userHomeViewModel.user.collectAsState()
    val helpRequestStatus by userHomeViewModel.helpRequestStatus.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    Column{
        AppHeader()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFDF1D0))
                .padding(bottom = 48.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {}
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 106.dp)
            .padding(bottom = 16.dp)
    ) {

        user?.let { user ->
            UserCard(navController, user)

            Spacer(modifier = Modifier.height(8.dp))

            AppealTabs(selectedTab) { newTab ->
                selectedTab = newTab
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (helpRequestStatus) {
                is UserHomeViewModel.Result.Success -> {
                    SnackBarManager.showMessage(stringResource(R.string.your_help_request_was_successful))

                }

                is UserHomeViewModel.Result.Failure -> {
                    Text(
                        text = "Error: ${(helpRequestStatus as UserHomeViewModel.Result.Failure).errorMessage}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is UserHomeViewModel.Result.Pending -> {
                }
            }

            if (user != null) {
                if (selectedTab == 0) {
                    UrgentAppealContent(user!!, userHomeViewModel)
                } else {
                    SpecificAppealContent(navController)
                }
            } else {
                Text("Loading user data...")
            }
        } ?:
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}



@Composable
fun UserCard(
    navController: NavController,
    user: User,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFF22B8F5)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White)
                ) {
                    Image(
                        painter = rememberImagePainter(user.userPhoto),
                        contentDescription = "User Photo",
                        modifier = Modifier.padding(2.dp)
                            .size(50.dp)
                            .fillMaxWidth()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Hello!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = user.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* إشعارات */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
            IconButton(onClick = { navController.navigate(AppDestination.ProfileDetailsDestination.route) }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AppealTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        TabItem("Urgent Appeal", selectedTab == 0) { onTabSelected(0) }
        TabItem("Specific Appeal", selectedTab == 1) { onTabSelected(1) }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF22B8F5) else Color.Gray,

        )
        if (isSelected) {
            Spacer(
                modifier = Modifier.padding(top = 4.dp)
                    .height(2.dp)
                    .width(170.dp)
                    .background(Color(0xFF22B8F5))
            )
        }else{
            Spacer(
                modifier = Modifier.padding(top = 4.dp)
                    .height(2.dp)
                    .width(170.dp)
                    .background(Color.Gray)
            )
        }
    }
}

@Composable
fun UrgentAppealContent(user: User, userHomeViewModel: UserHomeViewModel) {

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // متغير لتخزين الموقع
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var currentTime by remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_siren),
            contentDescription = "Siren",
            modifier = Modifier.size(280.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
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

                        userHomeViewModel.createUrgentHelpRequest(
                            user.copy(
                                addressMaps = "${currentLocation?.first},${currentLocation?.second}",
                                timeOfRequest = currentTime,
                                typeOfRequest = "Urgent",
                                statusRequest = "Being Processed"
                            )
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(colorButtonRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .padding(16.dp)
                .padding(horizontal = 46.dp)
        ) {
            Text(
                text = "Help Request",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SpecificAppealContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmergencyOption(
            title = "Medical",
            imageRes = R.drawable.ambulance_pana,
            backgroundColor = Color(0xFF63D759)
        ) {
            navController.navigate(AppDestination.MedicalEmergencyDestination.route)
        }

        PoliceEmergencyOption(
            title = "Police",
            imageRes = R.drawable.police_car_rafiki,
            backgroundColor = Color(0xFF57BFF3)
        ) {
            navController.navigate(AppDestination.PoliceEmergencyDestination.route)
        }

        EmergencyOption(
            title = "Fire",
            imageRes = R.drawable.fire_emergency,
            backgroundColor = Color(0xFFF39357)
        ) {
            navController.navigate(AppDestination.FireEmergencyDestination.route)
        }
    }
}


@Composable
fun EmergencyOption(
    title: String,
    imageRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }, // ✅ تنفيذ التنقل عند النقر
        colors = CardDefaults.cardColors(backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Emergency",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(160.dp)
            )
        }
    }
}

@Composable
fun PoliceEmergencyOption(
    title: String,
    imageRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit // ✅ تمرير حدث النقر
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }, // ✅ تنفيذ التنقل عند النقر
        colors = CardDefaults.cardColors(backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(160.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Emergency",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUserHomeScreen() {
    EmergencyAppTheme {
        UserHomeScreen(navController = rememberNavController())
    }
}
