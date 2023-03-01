package com.markusw.loginwithfirebase.domain.use_cases

import com.markusw.loginwithfirebase.domain.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    companion object {
        const val PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{6,}\$"
        const val MINIMUM_PASSWORD_LENGTH = 6
    }

    operator fun invoke(password: String): ValidationResult {
        if(password.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = "Password cannot be empty"
            )
        }

        if(password.length < MINIMUM_PASSWORD_LENGTH) {
            return ValidationResult(
                success = false,
                errorMessage = "Password must be at least 6 characters long"
            )
        }

        if(!password.matches(Regex(PASSWORD_REGEX))) {
            return ValidationResult(
                success = false,
                errorMessage = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
            )
        }

        return ValidationResult(success = true)
    }
}