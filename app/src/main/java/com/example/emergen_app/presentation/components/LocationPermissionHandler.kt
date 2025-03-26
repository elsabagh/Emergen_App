package com.example.emergen_app.presentation.components

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.emergen_app.ui.theme.adminWelcomeCard
import com.example.emergen_app.utils.isLocationPermissionGranted

@Composable
fun LocationPermissionHandler() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isLocationPermissionGranted(context)) {
            showDialog = true
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* لا تفعل شيء لإجبار المستخدم */ },
            title = { Text("Location Permission Required") },
            text = { Text("This app requires location access to function properly.") },
            confirmButton = {
                Button(
                    onClick = {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        showDialog = false
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(adminWelcomeCard),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Allow")
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = android.net.Uri.parse("package:${context.packageName}")
                        })
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(adminWelcomeCard),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Open Settings")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationPermissionHandler() {
    LocationPermissionHandler()
}