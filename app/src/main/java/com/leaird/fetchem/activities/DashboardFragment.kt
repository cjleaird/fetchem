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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.leaird.fetchem.R
import com.leaird.fetchem.activities.adapter.PuppyListAdapter
import com.leaird.fetchem.databinding.FragmentDashboardBinding
import com.leaird.fetchem.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by activityViewModels()

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Without this, the navigation controller does not handle the
        // back stack properly when switching tabs. Instead of returning
        // to the *home* tab on back press, it navigates to the last tab
        // selected.
        findNavController().graph.setStartDestination(R.id.dashboard)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val adapter = PuppyListAdapter()
        binding.puppyListsRv.apply {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        binding.viewAllMb.setOnClickListener {
            val action = DashboardFragmentDirections.actionDashboardToPuppyUploadFragment()
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.puppyLists
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                    .collect { puppyItems ->
                        adapter.submitList(puppyItems)
                    }
            }
            launch {
                viewModel.isLoading
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                    .collect { isLoading ->
                        if (isLoading) {
                            binding.dashboardProgressIndicator.show()
                        } else {
                            binding.dashboardProgressIndicator.hide()
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