package com.markusw.loginwithfirebase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.loginwithfirebase.domain.AuthenticationEvent
import com.markusw.loginwithfirebase.domain.model.User
import com.markusw.loginwithfirebase.data.network.services.FirebaseSignUpMethod
import com.markusw.loginwithfirebase.domain.use_cases.UserRegistrator
import com.markusw.loginwithfirebase.domain.use_cases.ValidateEmail
import com.markusw.loginwithfirebase.domain.use_cases.ValidatePassword
import com.markusw.loginwithfirebase.ui.view.screens.login.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val firebaseSignUpMethod: FirebaseSignUpMethod,
    private val userRegistrator: UserRegistrator
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private val channel = Channel<AuthenticationEvent>()
    val authenticationEventChannel = channel.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onRegister() {
        viewModelScope.launch {
            val emailResult = validateEmail(_uiState.value.email)
            val passwordResult = validatePassword(_uiState.value.password)
            val isAnyError = listOf(
                emailResult,
                passwordResult
            ).any { !it.success }

            if (isAnyError) {
                _uiState.update {
                    it.copy(
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val authResult = userRegistrator.registerUser(
                user = User(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                ),
                signUpMethod = firebaseSignUpMethod
            )

            _uiState.update {
                it.copy(
                    emailError = null,
                    passwordError = null,
                    isLoading = false
                )
            }

            if (!authResult.success) {
                channel.send(
                    element = AuthenticationEvent.Failure(
                        authResult.errorMessage ?: "Unknown error"
                    )
                )
                return@launch
            }

            channel.send(element = AuthenticationEvent.Success)
            resetUiState()
        }
    }

    fun resetUiState() {
        _uiState.update { UiState() }
    }

}