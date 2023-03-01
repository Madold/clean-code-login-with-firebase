package com.markusw.loginwithfirebase.domain.use_cases

import android.util.Patterns
import com.markusw.loginwithfirebase.domain.ValidationResult
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {

        if(email.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = "Email cannot be empty"
            )
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                success = false,
                errorMessage = "Email is not valid"
            )
        }

        return ValidationResult(success = true)
    }
}