package com.example.emergen_app.presentation.user.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.emergen_app.R
import com.example.emergen_app.data.models.User
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.ui.theme.EmergencyAppTheme
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader()

        UserCard(navController) // ✅ تمرير navController


        Spacer(modifier = Modifier.height(8.dp))

        AppealTabs(selectedTab) { newTab ->
            selectedTab = newTab
        }

        Spacer(modifier = Modifier.height(16.dp))

        // التعامل مع حالة الطلب (مراقبة الـ StateFlow)
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


    }
}

@Composable
fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFDF1D0)) // اللون البيج الفاتح
            .padding(top = 46.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.group_366), // استبدل بالأيقونة الصحيحة
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Emergency APP",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "For Deaf and Mute People",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun UserCard(navController: NavController) { // ✅ تمرير navController هنا
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(Color(0xFF22B8F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Hello!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Osman Yasser mohamed",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            IconButton(onClick = { /* إشعارات */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
            IconButton(onClick = { navController.navigate(AppDestination.ProfileDetailsDestination.route) }) { // ✅ التنقل عند الضغط
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
        horizontalArrangement = Arrangement.SpaceEvenly
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
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF22B8F5) else Color.Gray
        )
        if (isSelected) {
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .width(120.dp)
                    .background(Color(0xFF22B8F5))
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
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val isGpsEnabled = checkIfGpsEnabled(context)
                if (!isGpsEnabled) {
                    Log.e("LocationDebug", "GPS is disabled")
                    SnackBarManager.showMessage(R.string.please_enable_gps)
                }else{
                    fetchLocation(fusedLocationClient, context) { latitude, longitude ->
                        currentLocation = Pair(latitude, longitude)

                        val currentDate = Date()
                        val dateFormat =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        currentTime = dateFormat.format(currentDate)

                        userHomeViewModel.createUrgentHelpRequest(
                            user.copy(
                                addressMaps = "${currentLocation?.first},${currentLocation?.second}",
                                timeOfRequest = currentTime
                            )
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Red),
            modifier = Modifier.padding(16.dp)
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
            title = "Medical Emergency",
            imageRes = R.drawable.ambulance_pana,
            backgroundColor = Color(0xFF60D394)
        ) {
            navController.navigate(AppDestination.MedicalEmergencyDestination.route) // ✅ تنقل للطوارئ الطبية
        }

        EmergencyOption(
            title = "Police Emergency",
            imageRes = R.drawable.police_car_rafiki,
            backgroundColor = Color(0xFF4285F4)
        ) {
            navController.navigate(AppDestination.PoliceEmergencyDestination.route) // ✅ تنقل للشرطة
        }

        EmergencyOption(
            title = "Fire Emergency",
            imageRes = R.drawable.fire_emergency,
            backgroundColor = Color(0xFFFF6F61)
        ) {
            navController.navigate(AppDestination.FireEmergencyDestination.route) // ✅ تنقل للحريق
        }
    }
}


@Composable
fun EmergencyOption(
    title: String,
    imageRes: Int,
    backgroundColor: Color,
    onClick: () -> Unit // ✅ تمرير حدث النقر
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }, // ✅ تنفيذ التنقل عند النقر
        colors = CardDefaults.cardColors(backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(80.dp)
            )
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
