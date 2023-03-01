package com.markusw.loginwithfirebase.data.network.services

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleAuthMethod @Inject constructor(): SignInMethod() {

    override suspend fun authenticate(user: User): AuthenticationResult {
        return try {
            val task = firebaseAuth.signInWithCredential(user.token as AuthCredential).await()

            task.user?.let {
                AuthenticationResult(
                    success = true,
                )
            } ?: AuthenticationResult(
                success = false,
                errorMessage = "User is null"
            )

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthenticationResult(
                success = false,
                errorMessage = e.message
            )
        }
    }
}