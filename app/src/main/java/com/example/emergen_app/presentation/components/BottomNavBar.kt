package com.example.emergen_app.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorBottomNavBar

@Composable
fun BottomNavBar(
    bottomNavController: NavHostController,
) {

    val items = listOf(
        BottomItem(
            title = "Home",
            icon = Icons.Rounded.Home
        ),
        BottomItem(
            title = "Report",
            icon = Icons.Rounded.Report
        ), BottomItem(
            title = "Contract",
            icon = Icons.Rounded.Chat
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorBottomNavBar)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items.forEachIndexed { index, bottomItem ->
                    NavigationBarItem(
                        selected = selected.intValue == index,
                        onClick = {
                            selected.intValue = index
                            when (selected.intValue) {
                                0 -> {
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(
                                        AppDestination.UserHomeDestination.route
                                    )
                                }

                                1 -> {
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(
                                        AppDestination.ReportDestination.route
                                    )
                                }

                                2 -> {
                                    bottomNavController.popBackStack()
                                    bottomNavController.navigate(
                                        AppDestination.ContractDestination.route
                                    )
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = bottomItem.icon,
                                contentDescription = bottomItem.title,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        label = {
                            Text(
                                text = bottomItem.title,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = adminWelcomeCard
                        )
                    )
                }
            }

        }
    }
}

data class BottomItem(
    val title: String, val icon: ImageVector
)

@Preview()
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavBar(
        NavHostController(LocalContext.current)
    )
}

