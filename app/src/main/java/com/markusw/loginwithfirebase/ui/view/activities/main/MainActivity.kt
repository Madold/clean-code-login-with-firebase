package com.markusw.loginwithfirebase.ui.view.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markusw.loginwithfirebase.core.utils.Common.FirebaseAnalyticsInstance
import com.markusw.loginwithfirebase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}