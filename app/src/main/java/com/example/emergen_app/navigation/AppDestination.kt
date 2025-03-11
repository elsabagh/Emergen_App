package com.example.emergen_app.navigation

interface AppDestination {
    val route: String

    object SignInDestination : AppDestination {
        override val route = "SignIn"
    }

    object SignUpDestination : AppDestination {
        override val route = "SignUp"
    }

    object UserHomeDestination : AppDestination {
        override val route = "UserHome"
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

    object ContractDestination : AppDestination{
        override val route = "Contract"
    }

}