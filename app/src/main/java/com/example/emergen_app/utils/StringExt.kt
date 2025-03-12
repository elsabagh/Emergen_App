package com.example.emergen_app.utils

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"

fun String.isEmailValid() = this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String.isPasswordValid(): Boolean {
    return this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(confirmPassword: String): Boolean {
    return this == confirmPassword
}
