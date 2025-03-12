package com.example.emergen_app.presentation.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.presentation.user.contract.ContactScreen
import com.example.emergen_app.presentation.user.home.UserHomeScreen
import com.example.emergen_app.presentation.user.report.ReportScreen


@Composable
fun UserMainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedItem = selectedTab) { newItem ->
                selectedTab = newItem
                bottomNavController.navigate(newItem.route) {
                    launchSingleTop = true
                    restoreState = true
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
            composable(BottomNavItem.Report.route) { ReportScreen() }
            composable(BottomNavItem.Contract.route) { ContactScreen() }
        }
    }
}


enum class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    Home("Home", Icons.Default.Home, "home"),
    Report("Report", Icons.Default.Receipt, "report"),
    Contract("Contract", Icons.Default.Description, "contract")
}

@Composable
fun BottomNavigationBar(selectedItem: BottomNavItem, onItemSelected: (BottomNavItem) -> Unit) {
    NavigationBar {
        BottomNavItem.values().forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = item == selectedItem,
                onClick = { onItemSelected(item) }
            )
        }
    }
}