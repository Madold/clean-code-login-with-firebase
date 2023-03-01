package com.markusw.loginwithfirebase.core.ext

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this.requireContext(), message, duration).show()
}

