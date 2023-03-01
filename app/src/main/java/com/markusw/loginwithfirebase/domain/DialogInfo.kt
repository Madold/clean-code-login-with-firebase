package com.markusw.loginwithfirebase.domain

data class DialogInfo(
    val title: String,
    val message: String,
    val positiveButtonText: String? = null,
    val negativeButtonText: String? = null
)
