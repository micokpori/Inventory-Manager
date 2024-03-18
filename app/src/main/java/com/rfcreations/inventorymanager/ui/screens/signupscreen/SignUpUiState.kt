package com.rfcreations.inventorymanager.ui.screens.signupscreen

import com.google.firebase.auth.AuthResult
import com.rfcreations.inventorymanager.utils.Resource

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val authUiState: Resource<AuthResult> = Resource.Idle,
)
