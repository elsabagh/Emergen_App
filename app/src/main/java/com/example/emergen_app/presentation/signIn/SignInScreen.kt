package com.example.emergen_app.presentation.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emergen_app.R
import com.example.emergen_app.presentation.components.EmailField
import com.example.emergen_app.presentation.components.PasswordField
import com.example.emergen_app.ui.theme.EmergencyAppTheme
import com.example.emergen_app.ui.theme.adminWelcomeCard

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit = {},
    onSignUpClickNav: () -> Unit = {},
    onAdminSignIn: () -> Unit = {},
    onBranchSignIn: () -> Unit = {},
    onNavigateToHome: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val signInViewModel: SignInViewModel = hiltViewModel()
    val uiState by signInViewModel.uiState.collectAsStateWithLifecycle()
    val isSignInSucceeded by signInViewModel.isSignInSucceeded.collectAsStateWithLifecycle()
    val isAccountReady by signInViewModel.isAccountReady.collectAsStateWithLifecycle()
    val userRole by signInViewModel.userRole.collectAsStateWithLifecycle()

    LaunchedEffect(isSignInSucceeded) {
        if (isSignInSucceeded) {
            when (userRole) {
                "admin" -> onAdminSignIn()
                "branch" -> onBranchSignIn()
                "user" -> onSignInClick()
            }
            signInViewModel.resetIsSignInSucceeded()

        }
    }

    val onEmailChange: (String) -> Unit = remember { signInViewModel::onEmailChange }
    val onPasswordChange: (String) -> Unit = remember { signInViewModel::onPasswordChange }

    SignInScreenContent(
        uiState = uiState,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onSignInClick = { signInViewModel.signInToAccount("user") },
        onAdminClick = { signInViewModel.signInToAccount("admin") },
        onBranchClick = { signInViewModel.signInToAccount("branch") },
        onSignUpClick = onSignUpClickNav,
        onAppStart = { userRole?.let { onNavigateToHome(it) } }
    )
}

@Composable
fun SignInScreenContent(
    uiState: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onAdminClick: () -> Unit,
    onBranchClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAppStart: () -> Unit
) {
    LaunchedEffect(Unit) {
        onAppStart()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        Image(
            painter = painterResource(id = R.drawable.group_366),
            contentDescription = null,
            modifier = modifier
                .padding(top = 86.dp)
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Emergency App",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = "For Deaf and Mute People",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )

        EmailField(
            value = uiState.email,
            onNewValue = onEmailChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 64.dp)

        )

        Spacer(modifier = Modifier.padding(16.dp))

        PasswordField(
            value = uiState.password,
            placeholder = R.string.password,
            onNewValue = onPasswordChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(adminWelcomeCard),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(
                    onClick = {
                        onSignUpClick()
                    }
                )
                .align(Alignment.CenterHorizontally),
            text = buildAnnotatedString {
                append(stringResource(R.string.don_t_have_an_account_sign_up))
                withStyle(
                    style = SpanStyle(color = adminWelcomeCard),
                )
                {
                    append(" Sign up")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Row(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .padding(top = 46.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                modifier = Modifier.clickable { onAdminClick() },
                text = "Admin",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                modifier = Modifier.clickable { onBranchClick() },
                text = "Branch",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview() {
    EmergencyAppTheme {
        SignInScreenContent(
            uiState = SignInState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onAdminClick = {},
            onBranchClick = {},
            onSignUpClick = {},
            onAppStart = {}
        )
    }

}