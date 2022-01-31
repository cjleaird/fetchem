package com.leaird.fetchem.activities

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.leaird.fetchem.databinding.FragmentSplashBinding
import com.leaird.fetchem.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: SplashViewModel by activityViewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var logoAvd: AnimatedVectorDrawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        logoAvd = binding.iconIv.drawable as AnimatedVectorDrawable
        logoAvd.start()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFinished
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                .collect {
                    if (it) {
                        logoAvd.stop()
                        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
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