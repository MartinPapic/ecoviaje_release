package com.appecoviaje.viewmodel

import com.appecoviaje.data.UserPreferencesRepository
import com.appecoviaje.data.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val userPreferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(userPreferencesRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials succeeds`() = runTest {
        // Given
        val hashedPassword = "password".hashCode().toString()
        coEvery { userRepository.login("user", hashedPassword) } returns true

        // When
        viewModel.onUsernameChange("user")
        viewModel.onPasswordChange("password")
        viewModel.onLoginClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value.loginSuccess)
        coVerify(exactly = 1) { userPreferencesRepository.saveUserData(any(), any()) }
    }

    @Test
    fun `login with empty fields shows error`() = runTest {
        // When
        viewModel.onLoginClick() // Empty fields

        // Then
        assertTrue(viewModel.uiState.value.errorMessage.isNotEmpty())
    }
}
