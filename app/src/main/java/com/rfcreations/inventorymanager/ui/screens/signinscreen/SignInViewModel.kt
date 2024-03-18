package com.rfcreations.inventorymanager.ui.screens.signinscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.rfcreations.inventorymanager.repository.authrepository.AuthRepository
import com.rfcreations.inventorymanager.ui.screens.signupscreen.SignUpUiState
import com.rfcreations.inventorymanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signInUiState = MutableStateFlow(SignUpUiState())
    val signInUiState = _signInUiState.asStateFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        authRepository.login(email, password).collectLatest { result: Resource<AuthResult> ->
            when (result) {
                is Resource.Idle -> {}
                is Resource.Loading -> {
                    _signInUiState.value = _signInUiState.value.copy(authUiState = result)
                }
                is Resource.Success -> {
                    _signInUiState.value = _signInUiState.value.copy(authUiState = result)
                }
                is Resource.Error -> {
                    _signInUiState.value = _signInUiState.value.copy(authUiState = result)
                }
            }
        }
    }

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.UpdateEmail -> {
                _signInUiState.value = _signInUiState.value.copy(email = event.value)
            }
            is SignInUiEvent.UpdatePassword -> {
                _signInUiState.value = _signInUiState.value.copy(password = event.value)
            }
        }
    }
}
