package com.example.emergen_app.presentation.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.R
import com.example.emergen_app.presentation.components.BottomNavBar
import com.example.emergen_app.ui.theme.EmergencyAppTheme

@Composable
fun UserHomeScreen(
    navController: NavController,
) {

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader()

        UserCard()

        Spacer(modifier = Modifier.height(8.dp))

        AppealTabs(selectedTab) { newTab ->
            selectedTab = newTab
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (selectedTab == 0) {
                UrgentAppealContent()
            } else {
                SpecificAppealContent()
            }
        }


        BottomNavBar(bottomNavController = navController as NavHostController)

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
fun UserCard() {
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
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // استبدل بالصورة الصحيحة
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
            IconButton(onClick = { /* إشعارات */ }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Notifications",
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
fun UrgentAppealContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_siren),
            contentDescription = "Siren",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { /* إرسال طلب الاستغاثة */ },
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
fun SpecificAppealContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmergencyOption(
            title = "Medical Emergency",
            imageRes = R.drawable.ambulance_pana,
            backgroundColor = Color(0xFF60D394)
        )
        EmergencyOption(
            title = "Police Emergency",
            imageRes = R.drawable.police_car_rafiki,
            backgroundColor = Color(0xFF4285F4)
        )
        EmergencyOption(
            title = "Fire Emergency",
            imageRes = R.drawable.fire_emergency,
            backgroundColor = Color(0xFFFF6F61)
        )
    }
}

@Composable
fun EmergencyOption(title: String, imageRes: Int, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp),
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
