package com.example.emergen_app.presentation.admin.notification.userProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.colorCardProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController, userId: String) {
    val userViewModel: UserProfileViewModel = hiltViewModel()
    val user = userViewModel.getUserById(userId).collectAsState(initial = null)
    val openDialog = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf("") } // To hold the image URL for the dialog

    user.value?.let { user ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("User Profile") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // User photo with click listener to open the dialog
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Image(
                        painter = rememberImagePainter(user.userPhoto),
                        contentDescription = "User Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .fillMaxWidth()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Email",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorCardProfile)
                    ) {
                        Text(
                            text = user.email,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Mobile",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorCardProfile)
                    ) {
                        Text(
                            text = user.mobile,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Address",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorCardProfile)
                    ) {
                        Text(
                            text = user.governmentName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    Text(
                        text = "Location",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colorCardProfile)
                    ) {
                        Text(
                            text = user.addressMaps,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    Text(
                        text = "Upload ID",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        user.idFront?.let {
                            Image(
                                painter = rememberImagePainter(it),
                                contentDescription = "ID Front",
                                modifier = Modifier
                                    .size(100.dp) // Square size 100x100
                                    .clip(RectangleShape)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RectangleShape
                                    )
                                    .clickable {
                                        selectedImage.value =
                                            it // Set the image to be shown in dialog
                                        openDialog.value = true // Open the dialog
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                        user.idBack?.let {
                            Image(
                                painter = rememberImagePainter(it),
                                contentDescription = "ID Back",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RectangleShape)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RectangleShape
                                    )
                                    .clickable {
                                        selectedImage.value =
                                            it // Set the image to be shown in dialog
                                        openDialog.value = true // Open the dialog
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Display the dialog when openDialog is true
                    if (openDialog.value) {
                        Dialog(onDismissRequest = { openDialog.value = false }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Image(
                                    painter = rememberImagePainter(selectedImage.value),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clip(RectangleShape),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }

                // عرض الأزرار لتغيير الحالة أو حذف الحساب
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        userViewModel.acceptUser(userId = userId)
                        // الانتقال إلى AdminHome بعد قبول المستخدم
                        navController.navigate(AppDestination.AdminHomeDestination.route) {
                            popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                        }
                    }) {
                        Text("Accept")
                    }
                    Button(onClick = {
                        userViewModel.rejectUser(userId = userId)
                        // الانتقال إلى AdminHome بعد رفض المستخدم
                        navController.navigate(AppDestination.AdminHomeDestination.route) {
                            popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                        }
                    }) {
                        Text("Reject")
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    EmergencyAppTheme {
        UserProfileScreen(
            navController = rememberNavController(),
            userId = TODO(),
        )
    }
}
