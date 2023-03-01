package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.core.utils.Common.FirebaseAuthInstance
import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import javax.inject.Inject

abstract class SignUpMethod: RegisterService {
    protected val firebaseAuth = FirebaseAuthInstance

    abstract override suspend fun register(user: User): AuthenticationResult
}