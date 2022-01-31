package com.leaird.fetchem.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leaird.fetchem.activities.DashboardFragmentDirections
import com.leaird.fetchem.databinding.ItemPuppyListBinding

/**
 * Adapter used to display the distinct list_id values that are present in the fetched
 * JSON. We refer to these as puppy list items (groupings).
 */
class PuppyListAdapter : ListAdapter<Int, RecyclerView.ViewHolder>(PuppyListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PuppyListViewHolder(
            ItemPuppyListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PuppyListViewHolder).bind(plant)
    }

    inner class PuppyListViewHolder(private val binding: ItemPuppyListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.listId?.let {
                    val action =
                        DashboardFragmentDirections.actionDashboardToPuppyUploadFragment(
                            true,
                            true,
                            binding.puppyListIdTv.text.toString(),
                            it
                        )
                    binding.root.findNavController().navigate(action)
                }
            }
        }

        fun bind(item: Int) {
            binding.apply {
                listId = item
                executePendingBindings()
            }
        }
    }
}

private class PuppyListDiffCallback : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }
}