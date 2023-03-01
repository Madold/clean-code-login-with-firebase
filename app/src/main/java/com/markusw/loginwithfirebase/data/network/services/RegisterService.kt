package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User

interface RegisterService {
    suspend fun register(user: User): AuthenticationResult
}