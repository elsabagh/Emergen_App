package com.example.emergen_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emergen_app.ContainerAppState
import com.example.emergen_app.navigation.AppDestination.SignInDestination
import com.example.emergen_app.presentation.admin.home.AdminHomeScreen
import com.example.emergen_app.presentation.signIn.SignInScreen
import com.example.emergen_app.presentation.user.UserHomeScreen

@Composable
fun NavGraph(
    appState: ContainerAppState,
    userRole: String?,
) {
    val startDestination = when (userRole) {
        "admin" -> AppDestination.AdminHomeDestination.route
        "branch" -> AppDestination.BranchHomeDestination.route
        "user" -> AppDestination.UserHomeDestination.route
        else -> AppDestination.SignInDestination.route
    }

    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        composable(route = SignInDestination.route) {
            SignInScreen(
                onSignInClick = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.UserHomeDestination.route,
                        popUpToRoute = AppDestination.UserHomeDestination.route
                    )
                },
                onSignUpClickNav = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.SignUpDestination.route,
                        popUpToRoute = AppDestination.SignUpDestination.route
                    )
                },
                onAdminSignIn = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.AdminHomeDestination.route,
                        popUpToRoute = AppDestination.AdminHomeDestination.route
                    )
                },

            )
        }
        composable(route = AppDestination.AdminHomeDestination.route) {
            AdminHomeScreen(
                navController = appState.navController,
                onLogout = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = SignInDestination.route,
                        popUpToRoute = SignInDestination.route
                    )
                }
            )
        }
        composable(route = AppDestination.UserHomeDestination.route) {
            UserHomeScreen(
                navController = appState.navController
            )
        }
    }
}

