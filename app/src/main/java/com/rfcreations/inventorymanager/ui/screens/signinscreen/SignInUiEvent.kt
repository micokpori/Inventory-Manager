package com.rfcreations.inventorymanager.ui.screens.signinscreen

sealed class SignInUiEvent {
    data class UpdateEmail(val value: String) : SignInUiEvent()
    data class UpdatePassword(val value: String) : SignInUiEvent()
}
