package com.example.emergen_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmergencyAppTheme {
                Surface {
                    LocationPermissionHandler()
                    ContainerApp()
                }
            }
        }
    }
}

@Composable
fun LocationPermissionHandler() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // فحص الإذن عند التشغيل
    LaunchedEffect(Unit) {
        if (!isLocationPermissionGranted(context)) {
            showDialog = true
        }
    }

    // دالة تطلب الإذن من المستخدم
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
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Allow")
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = android.net.Uri.parse("package:${context.packageName}")
                        })
                        showDialog = false
                    }) {
                    Text("Open Settings")
                }
            }
        )
    }
}


fun isLocationPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}