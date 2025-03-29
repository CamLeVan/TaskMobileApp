package com.example.taskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Initial)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Initial)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            authRepository.login(email, password)
                .onSuccess {
                    _loginState.value = AuthState.Success
                }
                .onFailure { error ->
                    _loginState.value = AuthState.Error(error.message ?: "Login failed")
                }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            authRepository.register(name, email, password)
                .onSuccess {
                    _registerState.value = AuthState.Success
                }
                .onFailure { error ->
                    _registerState.value = AuthState.Error(error.message ?: "Registration failed")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
} 