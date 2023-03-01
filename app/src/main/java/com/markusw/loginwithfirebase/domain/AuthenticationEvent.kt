package com.markusw.loginwithfirebase.domain

sealed class AuthenticationEvent {
    object Success: AuthenticationEvent()
    data class Failure(val reason: String): AuthenticationEvent()
}
