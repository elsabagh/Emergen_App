package com.example.emergen_app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emergen_app.navigation.NavGraph
import com.example.emergen_app.presentation.signIn.SignInViewModel
import com.example.emergen_app.ui.theme.EmergencyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContainerApp(
    modifier: Modifier = Modifier,
) {
    val appState = rememberAppState()
    val signInViewModel: SignInViewModel = hiltViewModel()
    val userRole = signInViewModel.userRole.collectAsState().value

    EmergencyAppTheme {
        NavGraph(appState = appState, userRole = userRole)

    }
}
