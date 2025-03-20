package com.example.emergen_app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorBottomNavBar

@Composable
fun BottomNavigationBar(selectedItem: BottomNavItem, onItemSelected: (BottomNavItem) -> Unit) {

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
                BottomNavItem.values().forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon, contentDescription = item.title,
                                tint = if (item == selectedItem) adminWelcomeCard else Color.Black
                            )
                        },
                        label = {
                            Text(
                                item.title,
                                color = if (item == selectedItem) adminWelcomeCard else Color.Black
                            )
                        },
                        selected = item == selectedItem,
                        onClick = { onItemSelected(item) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomNavItem.values().forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .height(8.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                )
                            )
                            .background(
                                if (selectedItem.ordinal == index) adminWelcomeCard else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

enum class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    Home("Home", Icons.Default.Home, "home"),
    Report("Report", Icons.Default.Folder, "report"),
    Contact("Contact", Icons.Default.Chat, "contact")
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        selectedItem = BottomNavItem.Home,
        onItemSelected = {}
    )
}