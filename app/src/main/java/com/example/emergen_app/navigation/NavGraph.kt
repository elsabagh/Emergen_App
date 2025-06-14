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
import com.example.emergen_app.presentation.admin.branches.branchList.BranchListScreen
import com.example.emergen_app.presentation.admin.branches.branchList.branchDetails.BranchDetailsScreen
import com.example.emergen_app.presentation.admin.branches.editBranch.EditBranchScreen
import com.example.emergen_app.presentation.admin.contact.AdminChatScreen
import com.example.emergen_app.presentation.admin.contact.ListUserContactScreen
import com.example.emergen_app.presentation.admin.home.AdminHomeScreen
import com.example.emergen_app.presentation.admin.notification.NotificationScreen
import com.example.emergen_app.presentation.admin.reports.AdminReportScreen
import com.example.emergen_app.presentation.admin.userProfile.UserProfileScreen
import com.example.emergen_app.presentation.branch.BranchScreen
import com.example.emergen_app.presentation.branch.UserDetailsScreen
import com.example.emergen_app.presentation.branch.branchInfo.BranchInfoScreen
import com.example.emergen_app.presentation.signIn.SignInScreen
import com.example.emergen_app.presentation.signUp.SignupScreen
import com.example.emergen_app.presentation.user.UserChatScreen
import com.example.emergen_app.presentation.user.UserMainScreen
import com.example.emergen_app.presentation.user.home.notification.UserNotificationScreen
import com.example.emergen_app.presentation.user.home.specificAppeal.FireEmergency
import com.example.emergen_app.presentation.user.home.specificAppeal.MedicalEmergency
import com.example.emergen_app.presentation.user.home.specificAppeal.PoliceEmergency
import com.example.emergen_app.presentation.user.profile.EditProfile
import com.example.emergen_app.presentation.user.profile.ProfileDetails
import com.example.emergen_app.presentation.user.report.ReportScreen

@Composable
fun NavGraph(
    appState: ContainerAppState,
    userRole: String?,
    isAccountReady: Boolean,
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
                        !isAccountReady || userRole == null -> SignInDestination.route
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
                        route = SignUpDestination.route,
                        popUpToRoute = SignUpDestination.route
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
                        popUpToRoute = SignInDestination.route,
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
        composable(route = AppDestination.ReportDestination.route) {
            ReportScreen(
                navController = appState.navController
            )
        }
        composable(route = AppDestination.ContactDestination.route) {
            UserChatScreen(
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
        composable("branch_list/{branchName}") { backStackEntry ->
            val branchName = backStackEntry.arguments?.getString("branchName") ?: ""
            BranchListScreen(navController = appState.navController, branchName = branchName)
        }
        composable("add_branch/{branchName}") { backStackEntry ->
            val branchName = backStackEntry.arguments?.getString("branchName") ?: ""
            AddBranchScreen(navController = appState.navController, branchName = branchName)
        }

        composable("edit_branch/{branchId}") { backStackEntry ->
            val branchId = backStackEntry.arguments?.getString("branchId") ?: ""
            EditBranchScreen(
                navController = appState.navController,
                branchId = branchId // ✅ تمرير ID للصفحة
            )
        }


        composable(AppDestination.BranchHomeDestination.route) {
            BranchScreen(navController = appState.navController)
        }
        composable("branch_details/{branchId}") { backStackEntry ->
            val branchId = backStackEntry.arguments?.getString("branchId") ?: ""
            BranchDetailsScreen(navController = appState.navController, branchId = branchId)
        }

        composable(AppDestination.FireEmergencyDestination.route) {
            FireEmergency(
                navController = appState.navController
            )
        }
        composable(AppDestination.MedicalEmergencyDestination.route) {
            MedicalEmergency(
                navController = appState.navController
            )
        }
        composable(AppDestination.PoliceEmergencyDestination.route) {
            PoliceEmergency(
                navController = appState.navController
            )
        }

        composable(AppDestination.ProfileDetailsDestination.route) {
            ProfileDetails(
                navController = appState.navController
            )
        }
        composable("Edit_Profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            EditProfile(
                navController = appState.navController,
                userId = userId
            )
        }
        composable(AppDestination.UserNotificationDestination.route) {
            UserNotificationScreen(
                navController = appState.navController
            )
        }
        composable(AppDestination.BranchInfoDestination.route) {
            BranchInfoScreen(navController = appState.navController)
        }

        composable("user_details/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserDetailsScreen(
                navController = appState.navController,
                userId = userId
            )  // الانتقال إلى شاشة تفاصيل المستخدم
        }

        composable(AppDestination.AdminReportDestination.route) {
            AdminReportScreen(
                navController = appState.navController
            )
        }
        composable(AppDestination.ListContactDestination.route) {
            ListUserContactScreen(navController = appState.navController)
        }

        // شاشة محادثة الأدمن مع المستخدم
        composable("chat/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AdminChatScreen(navController = appState.navController, userId = userId)
        }


    }
}

