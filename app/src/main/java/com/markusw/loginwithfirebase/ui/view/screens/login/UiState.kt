package com.markusw.loginwithfirebase.ui.view.screens.login

data class UiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false
)
