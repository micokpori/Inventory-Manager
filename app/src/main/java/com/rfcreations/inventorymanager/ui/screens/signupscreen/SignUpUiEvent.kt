package com.rfcreations.inventorymanager.ui.screens.signupscreen

sealed class SignUpUiEvent {
    data class UpdateName(val value:String) : SignUpUiEvent()
    data class UpdateEmail(val value:String) : SignUpUiEvent()
    data class UpdatePassword(val value:String) : SignUpUiEvent()
}
