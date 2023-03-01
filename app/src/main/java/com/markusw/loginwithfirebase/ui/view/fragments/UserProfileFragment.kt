package com.markusw.loginwithfirebase.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.compose.SubcomposeAsyncImage
import com.markusw.loginwithfirebase.core.ext.toast
import com.markusw.loginwithfirebase.core.utils.Common.FirebaseAuthInstance
import com.markusw.loginwithfirebase.databinding.FragmentUserProfileBinding
import com.markusw.loginwithfirebase.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth = FirebaseAuthInstance
    private val user = firebaseAuth.currentUser
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        setupEventListeners()
        subscribeObservers()
    }

    private fun setupComponents() {
        binding.profileImageComposeView.setContent {
            AppTheme {
                SubcomposeAsyncImage(
                    model = user?.photoUrl.toString(),
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }
        }

        binding.profileNameTextView.text = user?.displayName
        binding.profileEmailTextView.text = user?.email
        binding.profileIdTextView.text = user?.uid

    }

    private fun setupEventListeners() {
        binding.logoutBtn.setOnClickListener { onLogout() }
    }

    private fun subscribeObservers() {

    }

    private fun onLogout() {
        lifecycleScope.launchWhenStarted {
            firebaseAuth.signOut()
            toast("Logged out successfully")
            delay(400)
            navController.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}