package com.example.emergen_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val isAccountReady = signInViewModel.isAccountReady.collectAsState().value

    EmergencyAppTheme {
        Box(modifier = modifier.fillMaxSize().padding(top = 36.dp)) {
            // ✅ `NavGraph` لعرض المحتوى الرئيسي
            NavGraph(
                appState = appState,
                userRole = userRole,
                isAccountReady = isAccountReady
            )

            // ✅ `SnackbarHost` لعرض الإشعارات في مقدمة الشاشة
            SnackbarHost(
                hostState = appState.scaffoldState.snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter), // ✅ ضمان أن يكون Snackbar أسفل الشاشة
                snackbar = { snackBarData ->
                    Snackbar(
                        snackBarData,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
}