package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.core.utils.Common.FirebaseAuthInstance
import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User

abstract class SignInMethod: AuthService {
    protected val firebaseAuth = FirebaseAuthInstance

    abstract override suspend fun authenticate(user: User): AuthenticationResult
}

