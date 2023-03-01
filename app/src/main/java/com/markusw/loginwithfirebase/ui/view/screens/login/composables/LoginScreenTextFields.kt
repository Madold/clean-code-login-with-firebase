package com.markusw.loginwithfirebase.ui.view.screens.login.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreenTextFields(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    isEmailValid: Boolean = false,
    emailError: String? = null,
    isPasswordValid: Boolean = false,
    passwordError: String? = null,
    onEmailDone: () -> Unit = {},
    onPasswordDone: () -> Unit = {},
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmailField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChanged,
            isError = isEmailValid,
            errorMessage = emailError,
            onDone = onEmailDone,
        )
        PasswordField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChanged,
            isError = isPasswordValid,
            errorMessage = passwordError,
            onDone = onPasswordDone,
        )
    }

}