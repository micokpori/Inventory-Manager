package com.rfcreations.inventorymanager.ui.screens.signinscreen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rfcreations.inventorymanager.ui.commons.EmailTextField
import com.rfcreations.inventorymanager.ui.commons.PasswordTextField
import com.rfcreations.inventorymanager.utils.Resource

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navToHomeScreen: () -> Unit,
    navToSignUpScreen: () -> Unit,
    navToForgotPasswordScreen: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val signUpUiState by signInViewModel.signInUiState.collectAsState()
    val email = signUpUiState.email
    val password = signUpUiState.password
    val authUiState = signUpUiState.authUiState
    var isSigningUp by rememberSaveable { mutableStateOf(false) }
    if (isSigningUp) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is Resource.Idle -> {}
            is Resource.Loading -> {
                isSigningUp = true
            }

            is Resource.Success -> {
                val firebaseAuth = FirebaseAuth.getInstance()
                val result = authUiState.data.user?.uid
                if (firebaseAuth.currentUser?.isEmailVerified == true) {
                    Log.d("user", result.toString())
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                        .show()
                    isSigningUp = false
                    navToHomeScreen()
                } else {
                    firebaseAuth.currentUser?.sendEmailVerification()
                    Toast.makeText(
                        context,
                        "verification email sent, please verify to continue",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    isSigningUp = false
                }
            }

            is Resource.Error -> {
                Toast.makeText(context, "E: ${authUiState.message}", Toast.LENGTH_SHORT).show()
                isSigningUp = false
            }
        }
    }

    Scaffold(modifier) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Welcome\nBack!",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall
            )
            EmailTextField(value = email) {
                signInViewModel.onEvent(SignInUiEvent.UpdateEmail(it))
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                PasswordTextField(value = password) {
                    signInViewModel.onEvent(SignInUiEvent.UpdatePassword(it))
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Forgot password?",
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .clickable { navToForgotPasswordScreen() }
                            .padding(8.dp)
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { signInViewModel.loginUser(email, password) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }

            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                Text(text = "Don't have an account? ")
                Text(text = "Sign up", modifier = Modifier.clickable {
                    navToSignUpScreen()
                }, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}