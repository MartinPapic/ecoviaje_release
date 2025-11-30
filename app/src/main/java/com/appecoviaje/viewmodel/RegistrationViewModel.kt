package com.appecoviaje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appecoviaje.data.User
import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.data.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val registrationSuccess: Boolean = false
)

class RegistrationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = "") }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = "") }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = "") }
    }

    fun onRegistrationClick() {
        if (!isInputValid()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val hashedPassword = hashPassword(uiState.value.password)
            val success = userRepository.register(uiState.value.username, hashedPassword)
            
            if (success) {
                 userPreferencesRepository.saveUserData("2", uiState.value.username) // Mock ID
                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error en el registro.") }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val state = uiState.value
        if (state.username.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, rellena todos los campos.") }
            return false
        }
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden.") }
            return false
        }
        return true
    }
    private fun hashPassword(password: String): String {
        return password.hashCode().toString()
    }
}
