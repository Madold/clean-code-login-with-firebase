package com.markusw.loginwithfirebase.domain

data class GoogleSignInResult(
    val authResult: AuthenticationResult = AuthenticationResult(
        success = false
    ),
    val token: Any? = null
)