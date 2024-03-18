package com.rfcreations.inventorymanager.ui.screens.profilescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.rfcreations.inventorymanager.ui.commons.EmailTextField
import com.rfcreations.inventorymanager.ui.navigation.Screens

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    val firebase = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        if (firebase.currentUser == null) {
            navController.navigate(Screens.SignInScreen.route)
        }
    }
    Scaffold(topBar = {
        AppBar(
            navBackAction = { navController.popBackStack() },
            signOutAction = { firebase.signOut()
                navController.navigate(Screens.SignInScreen.route)}
        )
    }) {
        Box(modifier.padding(it).padding(20.dp), contentAlignment = Alignment.Center) {
            Column {
                EmailTextField(value = firebase.currentUser?.email.toString(), onValueChange = {})
            }

            SignOutButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp),
                signOutAction = {
                    firebase.signOut()
                    navController.navigate(Screens.SignInScreen.route)
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    navBackAction: () -> Unit,
    signOutAction: () -> Unit
) {
    var showDropDown by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = "Profile") },
        navigationIcon = {
            IconButton(navBackAction) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            DropdownMenu(
                expanded = showDropDown,
                onDismissRequest = { showDropDown = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Log out") }, onClick = signOutAction)
            }
        }
    )
}


@Composable
private fun SignOutButton(modifier: Modifier = Modifier, signOutAction: () -> Unit) {
    Button(
        onClick = signOutAction,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(12.dp),
        modifier = modifier
    ) {
        Text(text = "Log out")
    }
}
