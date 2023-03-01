package com.markusw.loginwithfirebase.domain

data class ValidationResult(
    val success: Boolean = false,
    val errorMessage: String? = null
)