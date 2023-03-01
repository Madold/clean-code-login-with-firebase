package com.markusw.loginwithfirebase.domain

data class AuthenticationResult(
    val success: Boolean,
    val errorMessage: String? = null
)