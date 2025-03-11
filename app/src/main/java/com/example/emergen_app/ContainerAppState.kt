package com.example.emergen_app

import android.content.res.Resources
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.emergen_app.presentation.components.snackbar.SnackBarManager
import com.example.emergen_app.presentation.components.snackbar.SnackBarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class ContainerAppState(
    val scaffoldState: BottomSheetScaffoldState,
    val navController: NavHostController,
    val snackBarManager: SnackBarManager,
    val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackBarManager.snackBarMessages.filterNotNull().collect { snackBarMessage ->
                val text = snackBarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(message = text)
                snackBarManager.clearSnackBarState()
            }
        }
    }

    fun navigateSingleTopToAndPopupTo(
        route: String,
        popUpToRoute: String
    ) {
        navController.navigate(route) {
            popUpTo(route = popUpToRoute) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackBarManager = SnackBarManager,
    resources: Resources = LocalContext.current.resources,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
        ContainerAppState(
            scaffoldState = scaffoldState,
            navController = navController,
            snackBarManager = snackBarManager,
            resources = resources,
            coroutineScope = coroutineScope
        )
    }