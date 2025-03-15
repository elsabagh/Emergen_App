package com.example.emergen_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emergen_app.ContainerAppState
import com.example.emergen_app.SplashScreen
import com.example.emergen_app.navigation.AppDestination.SignInDestination
import com.example.emergen_app.navigation.AppDestination.SignUpDestination
import com.example.emergen_app.presentation.admin.accounts.AccountsScreen
import com.example.emergen_app.presentation.admin.branches.AllBranchesScreen
import com.example.emergen_app.presentation.admin.branches.addBranch.AddBranchScreen
import com.example.emergen_app.presentation.admin.branches.branchDetails.BranchListScreen
import com.example.emergen_app.presentation.admin.home.AdminHomeScreen
import com.example.emergen_app.presentation.admin.notification.NotificationScreen
import com.example.emergen_app.presentation.admin.userProfile.UserProfileScreen
import com.example.emergen_app.presentation.branch.BranchScreen
import com.example.emergen_app.presentation.signIn.SignInScreen
import com.example.emergen_app.presentation.signUp.SignupScreen
import com.example.emergen_app.presentation.user.UserMainScreen

@Composable
fun NavGraph(
    appState: ContainerAppState,
    userRole: String?,
    isAccountReady: Boolean, // ✅ التأكد من الجاهزية قبل الانتقال
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = appState.navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        // ✅ شاشة البداية
        composable(route = "splash") {
            SplashScreen(
                isAccountReady = isAccountReady,
                userRole = userRole,
                onSplashFinished = {
                    val destination = when {
                        !isAccountReady || userRole == null -> AppDestination.SignInDestination.route
                        userRole == "admin" -> AppDestination.AdminHomeDestination.route
                        userRole == "branch" -> AppDestination.BranchHomeDestination.route
                        else -> AppDestination.UserHomeDestination.route
                    }
                    appState.navigateSingleTopToAndPopupTo(destination, "splash")
                }
            )
        }
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
                onBranchSignIn = {
                    appState.navigateSingleTopToAndPopupTo(
                        route = AppDestination.BranchHomeDestination.route,
                        popUpToRoute = AppDestination.BranchHomeDestination.route
                    )
                }

                )
        }
        composable(
            route = SignUpDestination.route
        ) {
            SignupScreen(
                navController = appState.navController,
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
                },
            )
        }
        composable(route = AppDestination.NotificationDestination.route) {
            NotificationScreen(navController = appState.navController)
        }

        composable(route = AppDestination.AllAccountsDestination.route) {
            AccountsScreen(navController = appState.navController)
        }
        composable(route = AppDestination.UserHomeDestination.route) {
            UserMainScreen(
                navController = appState.navController
            )
        }
        composable("user_profile/{userId}/{isFromNotification}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val isFromNotification =
                backStackEntry.arguments?.getString("isFromNotification")?.toBoolean() ?: false
            UserProfileScreen(
                navController = appState.navController,
                userId = userId,
                isFromNotification = isFromNotification
            )
        }
        composable(route = AppDestination.AllBranchesDestination.route) {
            AllBranchesScreen(navController = appState.navController)
        }
        composable("branch_details/{branchName}") { backStackEntry ->
            val branchName = backStackEntry.arguments?.getString("branchName") ?: ""
            BranchListScreen(navController = appState.navController, branchName = branchName)
        }
        composable("add_branch") {
            AddBranchScreen(navController = appState.navController)
        }
        composable(AppDestination.BranchHomeDestination.route) {
            BranchScreen()
        }


    }
}

