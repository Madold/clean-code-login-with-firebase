package com.markusw.loginwithfirebase.ui.view.screens.login.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterText(
    onClick: () -> Unit
) {
    Row {
        Text(text = "Don't have an account?")
        Text(
            text = "Register here",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onClick() }
        )
    }
}