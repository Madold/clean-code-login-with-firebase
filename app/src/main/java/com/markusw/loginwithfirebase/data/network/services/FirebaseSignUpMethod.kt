package com.markusw.loginwithfirebase.data.network.services

import com.markusw.loginwithfirebase.domain.AuthenticationResult
import com.markusw.loginwithfirebase.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignUpMethod @Inject constructor(): SignUpMethod() {

    override suspend fun register(user: User): AuthenticationResult {
        return try {
            val task = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()

            task.user?.let {
                AuthenticationResult(
                    success = true,
                )
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