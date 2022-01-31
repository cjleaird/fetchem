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
import androidx.recyclerview.widget.DividerItemDecoration
import com.leaird.fetchem.activities.adapter.PuppyUploadAdapter
import com.leaird.fetchem.databinding.FragmentPuppyUploadBinding
import com.leaird.fetchem.viewmodel.PuppyUploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PuppyUploadFragment : Fragment() {
    private val viewModel: PuppyUploadViewModel by activityViewModels()

    private var _binding: FragmentPuppyUploadBinding? = null
    private val binding get() = _binding!!

    private var listId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            listId = it.getInt(ARGUMENT_LIST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPuppyUploadBinding.inflate(inflater, container, false)

        val adapter = PuppyUploadAdapter()
        binding.puppyUploadRv.apply {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadPuppyItems(listId)
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
                .collectLatest { puppyItems ->
                    puppyItems.let {
                        adapter.submitData(puppyItems)
                    }
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGUMENT_LIST_ID = "list_id"
    }
}