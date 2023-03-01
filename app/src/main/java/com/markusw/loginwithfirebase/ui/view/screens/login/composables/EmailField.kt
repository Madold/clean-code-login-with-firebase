package com.markusw.loginwithfirebase.ui.view.screens.login.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markusw.loginwithfirebase.R

@Composable
fun EmailField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    onDone: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BaseField(
            modifier = modifier,
            value = value,
            label = { Text("Email") },
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onDone = onDone,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            isError = isError,
            singleLine = true
        )
        AnimatedVisibility(visible = isError) {
            ErrorText(errorMessage = errorMessage)
        }
    }
}