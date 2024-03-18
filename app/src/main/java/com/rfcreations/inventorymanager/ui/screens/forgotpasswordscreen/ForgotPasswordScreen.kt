package com.rfcreations.inventorymanager.ui.screens.forgotpasswordscreen

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import com.rfcreations.inventorymanager.ui.commons.EmailTextField
import com.rfcreations.inventorymanager.ui.commons.GenericAppBar

@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier, navBackAction: () -> Unit) {
    val firebaseAuth = FirebaseAuth.getInstance()
    var isSendingResetMail by rememberSaveable { mutableStateOf(false )}
    var email by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(modifier, topBar = {
        GenericAppBar(
            title = "",
            navBackIcon = Icons.AutoMirrored.Filled.ArrowBack,
            navBackAction = navBackAction
        )
    }) { paddingValues ->
        Box(modifier) {
            if (isSendingResetMail){
                Dialog(onDismissRequest = { /*TODO*/ }) {
                    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Forgot\npassword?",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall
                )

                EmailTextField(value = email) {
                    email = it
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Continue",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = {
                            isSendingResetMail = true
                            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                                Toast.makeText(context,"Reset email sent", Toast.LENGTH_SHORT).show()
                                isSendingResetMail = false
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }

        }
    }
}