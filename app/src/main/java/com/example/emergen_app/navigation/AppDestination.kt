package com.example.emergen_app.navigation

interface AppDestination {
    val route: String

    object SignInDestination : AppDestination {
        override val route = "SignIn"
    }

    object SignUpDestination : AppDestination {
        override val route = "SignUp"
    }

    object SplashDestination: AppDestination {
        override val route = "Splash"
    }
    object UserHomeDestination : AppDestination {
        override val route = "user_home"
    }
    object AdminHomeDestination: AppDestination {
        override val route = "AdminHome"
    }

    object BranchHomeDestination: AppDestination {
        override val route = "BranchHome"
    }

    object ReportDestination : AppDestination {
        override val route = "Report"
    }

    object ContactDestination : AppDestination{
        override val route = "Contact"
    }

    object ListContactDestination : AppDestination{
        override val route = "user_contacts"
    }

    object AdminReportDestination : AppDestination{
        override val route = "AdminReport"
    }

    object NotificationDestination : AppDestination{
        override val route = "Notification"
    }

    object AllAccountsDestination : AppDestination{
        override val route = " AllAccounts"
    }

    object AllBranchesDestination : AppDestination{
        override val route = " AllBranches"
    }

    object BranchListDestination : AppDestination{
        override val route = " BranchList"
    }

    object PoliceEmergencyDestination : AppDestination{
        override val route = " PoliceEmergency"
    }

    object MedicalEmergencyDestination : AppDestination{
        override val route = " MedicalEmergency"
    }

    object FireEmergencyDestination : AppDestination{
        override val route = " FireEmergency"
    }

    object ProfileDetailsDestination : AppDestination{
        override val route = " ProfileDetails"
    }
    object UserNotificationDestination : AppDestination{
        override val route = " UserNotification"
    }

    object EditProfileDestination : AppDestination{
        override val route = " EditProfile"
    }

    object BranchInfoDestination : AppDestination{
        override val route = " BranchInfo"
    }
}