package com.markusw.loginwithfirebase.domain.model

data class User(
    val email: String = "",
    val password: String = "",
    val token: Any? = null
)
