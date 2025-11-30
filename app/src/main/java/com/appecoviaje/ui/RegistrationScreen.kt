package com.appecoviaje.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.appecoviaje.R
import com.appecoviaje.theme.AppEcoViajeTheme
import com.appecoviaje.viewmodel.RegistrationViewModel

@Composable
fun RegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val registrationUiState by registrationViewModel.uiState.collectAsState()

    if (registrationUiState.registrationSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("login")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ecoviajelogo),
            contentDescription = "EcoViaje Logo",
            modifier = Modifier.size(250.dp) // Increased size
        )
        Text(text = "Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = registrationUiState.username,
            onValueChange = { username -> registrationViewModel.onUsernameChange(username) },
            label = { Text("Correo electrónico") },
            isError = registrationUiState.errorMessage.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registrationUiState.password,
            onValueChange = { password -> registrationViewModel.onPasswordChange(password) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = registrationUiState.errorMessage.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = registrationUiState.confirmPassword,
            onValueChange = { confirmPassword -> registrationViewModel.onConfirmPasswordChange(confirmPassword) },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = registrationUiState.errorMessage.isNotEmpty()
        )
        if (registrationUiState.errorMessage.isNotEmpty()) {
            Text(
                text = registrationUiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (registrationUiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                registrationViewModel.onRegistrationClick()
            }) {
                Text("Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    AppEcoViajeTheme {
        RegistrationScreen(rememberNavController())
    }
}
