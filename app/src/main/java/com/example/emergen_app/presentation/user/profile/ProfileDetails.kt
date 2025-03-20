package com.example.emergen_app.presentation.user.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.emergen_app.data.models.User
import com.example.emergen_app.navigation.AppDestination
import com.example.emergen_app.presentation.admin.userProfile.ImageDialog
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.ui.theme.colorButtonRed

@Composable
fun ProfileDetails(
    navController: NavController,
) {
    val viewModel: ProfileDetailsViewModel = hiltViewModel()
    val user by viewModel.user.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf("") } // To hold the image URL for the dialog

    Scaffold(
        topBar = { ProfileDetailsTopAppBar(navController, user) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                user?.let { user ->
                    ProfileContent(user, openDialog, selectedImage)
                    ImageDialog(openDialog, selectedImage)
                    LogoutButton(viewModel, navController)
                } ?: CircularProgressIndicator()
            }
        }
    )
}

@Composable
fun ProfileContent(
    user: User,
    openDialog: MutableState<Boolean>,
    selectedImage: MutableState<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Profile details (email, mobile, address, etc.)
        ProfileDetailCard("Email", user.email)
        ProfileDetailCard("Mobile", user.mobile)
        ProfileDetailCard("Address", user.governmentName)
        LocationText("Location (Google maps)", location = user.addressMaps)

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Upload ID", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(
                    bottom = 8.dp
                )
                .align(Alignment.Start)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

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
                text = "Location Link",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // تحويل الإحداثيات إلى رابط Google Maps بشكل صحيح
                        val uri = "geo:$locationText?q=$locationText"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    },
                color = adminWelcomeCard

            )
        }
//        // إضافة الأيقونة أسفل النص
//        IconButton(
//            onClick = {
//                // تحويل الإحداثيات إلى رابط Google Maps عند الضغط على الأيقونة
//                val uri = "geo:$locationText?q=$locationText"
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//                intent.setPackage("com.google.android.apps.maps")
//                context.startActivity(intent)
//            },
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(top = 8.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.LocationOn, // اختر أيقونة الموقع
//                contentDescription = "Open in Maps",
//                tint = adminWelcomeCard // اختيار اللون المناسب للأيقونة
//            )
//        }

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
                .align(Alignment.Start),
            fontSize = 14.sp
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
fun LogoutButton(
    userViewModel: ProfileDetailsViewModel,
    navController: NavController
) {
    Button(
        onClick = {
            userViewModel.signOutFromAccount()
            navController.navigate(AppDestination.SignInDestination.route) {
                popUpTo(AppDestination.SignInDestination.route) { inclusive = true }
            }
        },
        colors = ButtonDefaults.buttonColors(colorButtonRed),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Logout",
            fontSize = 18.sp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsTopAppBar(navController: NavController, user: User?) {
    TopAppBar(
        title = { Text("My Profile") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                val userId = user?.userId
                navController.navigate("Edit_Profile/${userId}")
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFF6DD)
        )
    )
}


