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
import com.appecoviaje.viewmodel.LoginViewModel
import com.appecoviaje.ui.ViewModelFactory

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val loginUiState by loginViewModel.uiState.collectAsState()

    if (loginUiState.loginSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("home")
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Iniciar Sesion", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = loginUiState.username,
            onValueChange = { username -> loginViewModel.onUsernameChange(username) },
            label = { Text("Nombre Usuario") },
            isError = loginUiState.errorMessage.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = loginUiState.password,
            onValueChange = { password -> loginViewModel.onPasswordChange(password) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = loginUiState.errorMessage.isNotEmpty()
        )
        if (loginUiState.errorMessage.isNotEmpty()) {
            Text(
                text = loginUiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (loginUiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                loginViewModel.onLoginClick()
            }) {
                Text("Iniciar Sesion")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("registration") }) {
                Text("No tienes una cuenta? Registrate")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppEcoViajeTheme {
        LoginScreen(rememberNavController())
    }
}
