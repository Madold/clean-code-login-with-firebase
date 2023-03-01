package com.markusw.loginwithfirebase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.loginwithfirebase.data.network.services.FirebaseAuthMethod
import com.markusw.loginwithfirebase.data.network.services.GoogleAuthMethod
import com.markusw.loginwithfirebase.domain.AuthenticationEvent
import com.markusw.loginwithfirebase.domain.model.User
import com.markusw.loginwithfirebase.domain.use_cases.UserAuthenticator
import com.markusw.loginwithfirebase.domain.use_cases.ValidateEmail
import com.markusw.loginwithfirebase.domain.use_cases.ValidatePassword
import com.markusw.loginwithfirebase.ui.view.screens.login.UiState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userAuthenticator: UserAuthenticator,
    private val firebaseAuthMethod: FirebaseAuthMethod,
    private val googleAuthMethod: GoogleAuthMethod,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
): ViewModel() {

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

    fun onLogin() {
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
            val authResult = userAuthenticator.authenticateUser(
                user = User(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                ),
                method = firebaseAuthMethod
            )
            _uiState.update { it.copy(isLoading = false) }

            if (!authResult.success) {
                channel.send(
                    AuthenticationEvent.Failure(
                        reason = authResult.errorMessage ?: "Unknown error"
                    )
                )
                return@launch
            }
            channel.send(AuthenticationEvent.Success)
            resetUiState()
        }
    }

    fun onGoogleLoginResult(token: Any) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val authResult = userAuthenticator.authenticateUser(
                user = User(
                    token = token
                ),
                method = googleAuthMethod
            )

            if (!authResult.success) {
                _uiState.update { it.copy(isLoading = false) }
                channel.send(
                    AuthenticationEvent.Failure(
                        reason = authResult.errorMessage ?: "Unknown error"
                    )
                )
                Logger.e(authResult.errorMessage ?: "Unknown error")
                return@launch
            }

            channel.send(AuthenticationEvent.Success)
            Logger.i("Google login success")
            resetUiState()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun resetUiState() {
        _uiState.update { UiState() }
    }

}