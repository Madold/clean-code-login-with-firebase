package com.markusw.loginwithfirebase.domain.use_cases

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import com.markusw.loginwithfirebase.data.network.services.AuthService
import javax.inject.Inject

class UserAuthenticator @Inject constructor() {
    suspend fun authenticateUser(user: User, method: AuthService): AuthenticationResult {
        return method.authenticate(user)
    }
}