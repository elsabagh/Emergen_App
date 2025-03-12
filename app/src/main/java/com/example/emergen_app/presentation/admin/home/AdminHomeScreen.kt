package com.example.emergen_app.presentation.admin.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.R
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.cardMenu


@Composable
fun AdminHomeScreen(
    navController: NavController,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // App Header (Logo + Title)


        val homeViewModel: AdminHomeViewModel = hiltViewModel()
        val isAccountSignedOut by homeViewModel.isAccountSignedOut.collectAsStateWithLifecycle()
        // ✅ بمجرد تسجيل الخروج، يتم التنقل إلى شاشة تسجيل الدخول
        LaunchedEffect(isAccountSignedOut) {
            if (isAccountSignedOut) {
                onLogout()
                homeViewModel.resetIsAccountSignedOut()
            }
        }

        Column(
            modifier = Modifier
                .background(Color(0xFFFDF1D0)) // Light beige background
        ) {
            AppHeader(navController) // ✅ تمرير `navController` إلى `AppHeader`

            Spacer(modifier = Modifier.height(16.dp))

            // Admin Welcome Card
            AdminWelcomeCard(
                homeViewModel,
                navController // ✅ تمرير `navController` حتى يمكن استخدامه داخل `AdminWelcomeCard`

            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Buttons
        MenuButton(icon = Icons.Default.Person, text = "All Accounts") {
            navController.navigate(AppDestination.AllAccountsDestination.route)
        }
        MenuButton(icon = Icons.Default.Business, text = "Branches") {
            navController.navigate("branches")
        }
        MenuButton(icon = Icons.Default.Chat, text = "Contact") {
            navController.navigate("contact")
        }
        MenuButton(icon = Icons.Default.Assignment, text = "Report Status") {
            navController.navigate("report_status")
        }
    }
}

@Composable
fun AppHeader(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.group_366), // Replace with your logo
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
fun AdminWelcomeCard(
    viewModel: AdminHomeViewModel,
    navController: NavController // ✅ تمرير `navController` إلى `AdminWelcomeCard`

) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),

        ) {
        Row(
            modifier = Modifier
                .background(adminWelcomeCard)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.group_371), // Replace with actual admin icon
                    contentDescription = "Admin Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hello!, Admin",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Row {
                IconButton(onClick = { navController.navigate(AppDestination.NotificationDestination.route) }) { // ✅ التنقل إلى صفحة الإشعارات
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { viewModel.signOutFromAccount() }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(cardMenu),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(74.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminHomeScreen() {
    EmergencyAppTheme {
        AdminHomeScreen(
            navController = rememberNavController(),
            onLogout = {}
        )
    }
}
