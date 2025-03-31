package com.example.munchies.ui.home.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.databinding.ItemFeedBinding
import com.example.munchies.model.Review
import com.example.munchies.ui.review.ReviewDetailsActivity

class FeedAdapter (val onLike : ((Review) -> Unit)  ) : ListAdapter<Review, FeedAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ReviewViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.feedContent.text = review.caption
            binding.userName.text = review.user.username
            binding.likeButton.setTag(review.liked)
            if(review.liked != null){
                if(review.liked!!){
                    binding.likeButton.setImageResource(R.drawable.ic_heart_orange_fill)
                }
            }
            binding.root.setOnClickListener {
                val context = binding.root.context

                val intent = Intent(context, ReviewDetailsActivity::class.java).apply {
                    putExtra("review", review)
                }
                context.startActivity(intent)
            }
            binding.likeButton.setOnClickListener{
                if (binding.likeButton.getTag() == false){
                    binding.likeButton.setTag(true)
                    binding.likeButton.setImageResource(R.drawable.ic_heart_orange_fill)
                }
                else{
                    binding.likeButton.setTag(false)
                    binding.likeButton.setImageResource(R.drawable.ic_heart_orange)

                }
                onLike(review)
            }
        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.reviewID == newItem.reviewID
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}
