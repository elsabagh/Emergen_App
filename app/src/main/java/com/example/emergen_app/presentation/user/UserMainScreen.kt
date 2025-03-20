package com.example.emergen_app.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.presentation.components.BottomNavItem
import com.example.emergen_app.presentation.components.BottomNavigationBar
import com.example.emergen_app.presentation.user.contact.ContactScreen
import com.example.emergen_app.presentation.user.home.UserHomeScreen
import com.example.emergen_app.presentation.user.report.ReportScreen


@Composable
fun UserMainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            Column {
                BottomNavigationBar(selectedItem = selectedTab) { newItem ->
                    selectedTab = newItem
                    bottomNavController.navigate(newItem.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { UserHomeScreen(navController) }
            composable(BottomNavItem.Report.route) { ReportScreen(navController) }
            composable(BottomNavItem.Contact.route) { ContactScreen(navController) }
        }
    }
}
