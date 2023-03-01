package com.markusw.loginwithfirebase.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.markusw.loginwithfirebase.core.ext.toast
import com.markusw.loginwithfirebase.core.utils.DialogBuilder
import com.markusw.loginwithfirebase.databinding.FragmentRegisterScreenBinding
import com.markusw.loginwithfirebase.domain.AuthenticationEvent
import com.markusw.loginwithfirebase.domain.DialogInfo
import com.markusw.loginwithfirebase.ui.view.screens.login.composables.LoginScreenTextFields
import com.markusw.loginwithfirebase.ui.viewmodel.RegisterScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class RegisterScreenFragment: Fragment() {

    private var _binding: FragmentRegisterScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterScreenViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        setupEventListeners()
        subscribeObservers()
    }

    private fun setupComponents() {
        binding.textFieldsComposeView.setContent {
            val uiState by viewModel.uiState.collectAsState()
            val focusManager = LocalFocusManager.current

            LoginScreenTextFields(
                email = uiState.email,
                password = uiState.password,
                onEmailChanged = viewModel::onEmailChanged,
                onPasswordChanged = viewModel::onPasswordChanged,
                isEmailValid = uiState.emailError != null,
                isPasswordValid = uiState.passwordError != null,
                emailError = uiState.emailError,
                passwordError = uiState.passwordError,
                onEmailDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onPasswordDone = {
                    focusManager.clearFocus()
                }
            )
        }
    }

    private fun setupEventListeners() {
        binding.registerBtn.setOnClickListener {
            viewModel.onRegister()
        }

        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.authenticationEventChannel.collect { event ->
                when (event) {
                    is AuthenticationEvent.Success -> {
                        toast(message = "Successfully registered")
                        delay(500)
                        navController.popBackStack()
                    }
                    is AuthenticationEvent.Failure -> {
                        DialogBuilder.buildDialog(
                            context = requireContext(),
                            dialogInfo = DialogInfo(
                                title = "Error",
                                message = event.reason,
                                positiveButtonText = "OK",
                            ),
                            onPositiveButtonClicked = { dialog, _ ->
                                dialog.dismiss()
                            },
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}