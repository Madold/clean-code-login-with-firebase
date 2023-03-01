package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User

interface AuthService {
    suspend fun authenticate(user: User): AuthenticationResult
}

