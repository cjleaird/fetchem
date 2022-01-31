package com.leaird.fetchem.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.leaird.fetchem.databinding.FragmentAccountBinding
import com.leaird.fetchem.model.prefs.UserPreferences
import com.leaird.fetchem.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.logoutMb.setOnClickListener {
            viewModel.logout()
        }

        binding.accountTv.text = UserPreferences.email

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user == null) {
                    val action =
                        AccountFragmentDirections.actionAccountFragmentToLoginFragment()
                    findNavController().navigate(action)
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