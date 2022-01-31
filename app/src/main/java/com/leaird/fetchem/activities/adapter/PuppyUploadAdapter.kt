package com.leaird.fetchem.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leaird.fetchem.databinding.ItemPuppyUploadBinding
import com.leaird.fetchem.model.db.PuppyItem

/**
 * Adapter used to display the puppy items. These items will be sorted by name.
 */
class PuppyUploadAdapter :
    PagingDataAdapter<PuppyItem, RecyclerView.ViewHolder>(PuppyUploadDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PuppyUploadViewHolder(
            ItemPuppyUploadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as PuppyUploadViewHolder).bind(item)
        }
    }

    inner class PuppyUploadViewHolder(private val binding: ItemPuppyUploadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PuppyItem) {
            binding.apply {
                this.item = item
                executePendingBindings()
            }
        }
    }
}

private class PuppyUploadDiffCallback : DiffUtil.ItemCallback<PuppyItem>() {
    override fun areItemsTheSame(oldItem: PuppyItem, newItem: PuppyItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PuppyItem, newItem: PuppyItem): Boolean {
        return oldItem.name == newItem.name
    }
}