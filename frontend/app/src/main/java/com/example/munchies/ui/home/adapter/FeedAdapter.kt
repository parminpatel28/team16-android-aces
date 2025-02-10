package com.example.munchies.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.databinding.ItemFeedBinding
import com.example.munchies.ui.home.model.FeedItem

class FeedAdapter : ListAdapter<FeedItem, FeedAdapter.FeedViewHolder>(FeedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feedItem = getItem(position)
        holder.bind(feedItem)
    }

    inner class FeedViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feedItem: FeedItem) {
            // binding.feedItem = feedItem
            // Load profile image and feed image (if URLs are used)

            // Set text views
            binding.userName.text = feedItem.title
            binding.feedContent.text = feedItem.content
        }
    }

    class FeedDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
        override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem == newItem
        }
    }
}
