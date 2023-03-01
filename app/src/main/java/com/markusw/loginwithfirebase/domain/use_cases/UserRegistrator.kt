package com.markusw.loginwithfirebase.domain.use_cases

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import com.markusw.loginwithfirebase.data.network.services.SignUpMethod
import javax.inject.Inject

class UserRegistrator @Inject constructor() {
    suspend fun registerUser(user: User, signUpMethod: SignUpMethod): AuthenticationResult {
        return signUpMethod.register(user)
    }
}