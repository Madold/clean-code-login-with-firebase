package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthMethod @Inject constructor(): SignInMethod() {

    override suspend fun authenticate(user: User): AuthenticationResult {
        return try {
            val task = firebaseAuth.signInWithEmailAndPassword(user.email, user.password).await()

            return task.user?.let {
                AuthenticationResult(success = true)
            } ?: AuthenticationResult(
                success = false,
                errorMessage = "User is null"
            )
        } catch (e: Exception) {
            AuthenticationResult(
                success = false,
                errorMessage = e.message
            )
        }
    }
}

