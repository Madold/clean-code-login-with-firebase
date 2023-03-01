package com.markusw.loginwithfirebase.ui.view.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.markusw.loginwithfirebase.R
import com.markusw.loginwithfirebase.core.ext.toast
import com.markusw.loginwithfirebase.core.utils.Common.FirebaseAuthInstance
import com.markusw.loginwithfirebase.core.utils.DialogBuilder
import com.markusw.loginwithfirebase.databinding.FragmentLoginScreenBinding
import com.markusw.loginwithfirebase.domain.AuthenticationEvent
import com.markusw.loginwithfirebase.domain.DialogInfo
import com.markusw.loginwithfirebase.ui.theme.AppTheme
import com.markusw.loginwithfirebase.ui.view.screens.login.composables.LoginScreenTextFields
import com.markusw.loginwithfirebase.ui.view.screens.login.composables.RegisterText
import com.markusw.loginwithfirebase.ui.viewmodel.LoginScreenViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginScreenViewModel by viewModels()
    private val navController by lazy { findNavController() }
    private val firebaseAuth = FirebaseAuthInstance
    private val launcher = registerForActivityResult(StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnCompleteListener {
                if(it.isSuccessful) {
                    val credential = GoogleAuthProvider.getCredential(it.result.idToken, null)
                    Logger.d("Google sign in was successful")
                    viewModel.onGoogleLoginResult(token = credential)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // If user is already logged in, navigate to user profile screen
        firebaseAuth.currentUser?.let {
            navController.navigate(R.id.action_loginScreenFragment_to_userProfileFragment)
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
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
            AppTheme {
                val uiState by viewModel.uiState.collectAsState()
                val focusManager = LocalFocusManager.current

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                    RegisterText(
                        onClick = {
                            navController.navigate(R.id.action_loginScreenFragment_to_registerScreenFragment)
                        }
                    )
                }
            }
        }
    }

    private fun setupEventListeners() {
        binding.loginBtn.setOnClickListener { viewModel.onLogin() }
        binding.googleLoginBtn.setOnClickListener {
            launcher.launch(getGoogleSignInClient().signInIntent)
        }

    }

    private fun subscribeObservers() {
        // Observes the authentication event channel
        lifecycleScope.launchWhenStarted {
            viewModel.authenticationEventChannel.collect { event ->
                when (event) {
                    is AuthenticationEvent.Failure -> {
                        DialogBuilder.buildDialog(
                            context = requireContext(),
                            dialogInfo = DialogInfo(
                                title = "Login failed",
                                message = event.reason,
                                positiveButtonText = "Ok",
                            ),
                            onPositiveButtonClicked = { dialog, _ ->
                                dialog.dismiss()
                            }
                        ).show()
                    }
                    is AuthenticationEvent.Success -> {
                        toast("Login successful")
                        Logger.i("Login successful")
                        delay(400)
                        navController.navigate(R.id.action_loginScreenFragment_to_userProfileFragment)
                    }
                }
            }
        }

        // Observes the uiState loading state to show/hide the loading screen
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                binding.loadingScreen.visibility = if(state.isLoading) View.VISIBLE else View.GONE
            }
        }

    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(requireContext(), buildGoogleSignInOptions())
    }

    private fun buildGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}