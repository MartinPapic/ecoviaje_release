package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.data.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { currentState ->
            currentState.copy(username = username)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    fun onLoginClick() {
        if (!isInputValid()) {
            _uiState.update { it.copy(errorMessage = "Por favor, ingresa usuario y contraseña.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val hashedPassword = hashPassword(uiState.value.password)
            val success = userRepository.login(uiState.value.username, hashedPassword)
            
            if (success) {
                // Fetch user details locally or from response (here we assume local sync or just use username)
                // For now, we save the session
                userPreferencesRepository.saveUserData("1", uiState.value.username) // Mock ID
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Usuario o contraseña inválidos (o error de red).") }
            }
        }
    }

    private fun isInputValid(): Boolean {
        return uiState.value.username.isNotBlank() && uiState.value.password.isNotBlank()
    }

    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }
}
