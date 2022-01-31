package com.leaird.fetchem.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.leaird.fetchem.databinding.FragmentLoginBinding
import com.leaird.fetchem.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginMb.setOnClickListener {
            viewModel.login(
                binding.usernameTiet.text?.toString() ?: "",
                binding.passwordTiet.text?.toString() ?: ""
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.showLogin
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                    .collect { showLogin ->
                        if (showLogin) {
                            binding.root.visibility = View.VISIBLE
                        }
                    }
            }
            launch {
                viewModel.currentUser.observe(viewLifecycleOwner) { user ->
                    if (user != null) {
                        // Check the logged in user's role. This will determine which
                        // fragment we navigate to.
                        val action: NavDirections = if (user.isAdmin) {
                            LoginFragmentDirections.actionLoginFragmentToDashboard()
                        } else {
                            LoginFragmentDirections.actionLoginFragmentToDiscover()
                        }
                        findNavController().navigate(action)
                    }
                }
            }
            launch {
                viewModel.errorMessage
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                    .collect { messageResId ->
                        if (messageResId != -1) {
                            binding.passwordTil.error = getString(messageResId)
                        }
                    }
            }
            launch {
                viewModel.isLoading
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                    .collectLatest { isLoading ->
                        if (isLoading) {
                            binding.loginProgressIndicator.visibility = View.VISIBLE
                        } else {
                            binding.loginProgressIndicator.visibility = View.INVISIBLE
                        }
                    }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}