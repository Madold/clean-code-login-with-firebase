package com.markusw.loginwithfirebase.ui.view.screens.login.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorText(
    errorMessage: String? = null,
) {
    Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
}