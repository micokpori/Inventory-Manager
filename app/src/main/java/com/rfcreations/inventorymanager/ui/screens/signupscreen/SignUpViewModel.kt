package com.rfcreations.inventorymanager.ui.screens.signupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.rfcreations.inventorymanager.repository.authrepository.AuthRepository
import com.rfcreations.inventorymanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){
    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    fun registerUser(email:String,password:String,name:String) = viewModelScope.launch {
        authRepository.register(email, password, name).collectLatest { result: Resource<AuthResult> ->
            when(result) {
                is Resource.Idle -> {}
                is Resource.Loading -> {
                    _signUpUiState.update {
                        it.copy(authUiState = result)
                    }
                }
                is Resource.Success -> {
                    _signUpUiState.update {
                        it.copy(authUiState = result)
                    }
                }
                is Resource.Error -> {
                    _signUpUiState.update {
                        it.copy(authUiState = result)
                    }
                }
            }
        }
    }


    fun onEvent(event: SignUpUiEvent){
        when(event){
            is SignUpUiEvent.UpdateName -> {
                _signUpUiState.update {
                    it.copy(name = event.value)
                }
            }
            is SignUpUiEvent.UpdateEmail -> {
                _signUpUiState.update {
                    it.copy(email = event.value)
                }
            }
            is SignUpUiEvent.UpdatePassword -> {
                _signUpUiState.update {
                    it.copy(password = event.value)
                }
            }
        }
    }
}