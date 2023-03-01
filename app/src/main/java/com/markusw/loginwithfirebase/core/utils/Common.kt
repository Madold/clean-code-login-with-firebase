package com.markusw.loginwithfirebase.core.utils

import android.provider.Settings.Global.getString
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.markusw.loginwithfirebase.R

object Common {
    val FirebaseAnalyticsInstance = Firebase.analytics
    val FirebaseAuthInstance = Firebase.auth
    val FirebaseCrashlyticsInstance = Firebase.crashlytics

}