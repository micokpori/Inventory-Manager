package com.rfcreations.inventorymanager.ui.screens.signinscreen

import com.google.firebase.auth.AuthResult
import com.rfcreations.inventorymanager.utils.Resource

data class SignInUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val authUiState: Resource<AuthResult> = Resource.Idle,
)