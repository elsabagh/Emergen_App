package com.example.emergen_app.presentation.admin.userProfile

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.data.models.User
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.colorButtonGreen
import com.example.emergen_app.ui.theme.colorButtonRed
import com.example.emergen_app.ui.theme.colorCardProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController, userId: String, isFromNotification: Boolean) {
    val userViewModel: UserProfileViewModel = hiltViewModel()
    val user = userViewModel.getUserById(userId).collectAsState(initial = null)
    val openDialog = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf("") } // To hold the image URL for the dialog

    user.value?.let { user ->
        Scaffold(
            topBar = { UserProfileTopAppBar(navController) },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserProfileContent(user, openDialog, selectedImage)
                    ImageDialog(openDialog, selectedImage)
                    ActionButtons(userViewModel, userId, navController, isFromNotification)
                }
            }
        )
    }
}

@Composable
fun UserProfileContent(
    user: User,
    openDialog: MutableState<Boolean>,
    selectedImage: MutableState<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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

        // Profile details (email, mobile, address, etc.)
        ProfileDetailCard("Email", user.email)
        ProfileDetailCard("Mobile", user.mobile)
        ProfileDetailCard("Address", user.governmentName)
        LocationText("Location", location = user.addressMaps)

        Spacer(modifier = Modifier.height(8.dp))


        // Upload ID images
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Start)
        ) {
            user.idFront?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "ID Front",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RectangleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape)
                        .clickable {
                            selectedImage.value = it
                            openDialog.value = true
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
                        .border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape)
                        .clickable {
                            selectedImage.value = it
                            openDialog.value = true
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
fun LocationText(label: String, location: String) {
    var locationText by remember { mutableStateOf(location) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = locationText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        // تحويل الإحداثيات إلى رابط Google Maps بشكل صحيح
                        val uri = "geo:$locationText?q=$locationText"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    }

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ProfileDetailCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ImageDialog(openDialog: MutableState<Boolean>, selectedImage: MutableState<String>) {
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


@Composable
fun ActionButtons(
    userViewModel: UserProfileViewModel,
    userId: String,
    navController: NavController,
    isFromNotification: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (isFromNotification) {
            // When coming from NotificationScreen, keep the Accept button active
            Button(
                onClick = {
                    userViewModel.acceptUser(userId = userId)
                    navController.navigate(AppDestination.AdminHomeDestination.route) {
                        popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(colorButtonGreen),
                modifier = Modifier.width(150.dp)
            ) {
                Text("Accept")
            }
        } else {
            // When coming from AccountsScreen, disable the Accept button and change user status
            Button(
                onClick = {
                    // Change user status to "Under Processing" when the button is clicked
                    userViewModel.disableUser(userId = userId)
                    navController.navigate(AppDestination.AdminHomeDestination.route) {
                        popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier.width(150.dp)
            ) {
                Text("Disable")
            }
        }

        if (isFromNotification) {
            Button(
                onClick = {
                    userViewModel.rejectUser(userId = userId)
                    navController.navigate(AppDestination.AdminHomeDestination.route) {
                        popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(colorButtonRed),
                modifier = Modifier.width(150.dp)
            ) {
                Text("Reject")
            }
        }else{
            Button(
                onClick = {
                    userViewModel.rejectUser(userId = userId)
                    navController.navigate(AppDestination.AdminHomeDestination.route) {
                        popUpTo(AppDestination.AdminHomeDestination.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(colorButtonRed),
                modifier = Modifier.width(150.dp)
            ) {
                Text("Delete")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileTopAppBar(navController: NavController) {
    TopAppBar(
        title = { Text("User Profile") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUserProfileScreen() {
    EmergencyAppTheme {
        UserProfileScreen(
            navController = rememberNavController(), userId = "1",
            isFromNotification = TODO(),
        )
    }
}
