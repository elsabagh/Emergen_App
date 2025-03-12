package com.example.emergen_app.presentation.user.contract


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContactScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Contract Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview
@Composable
fun PreviewContractScreen() {
    ContactScreen()
}
