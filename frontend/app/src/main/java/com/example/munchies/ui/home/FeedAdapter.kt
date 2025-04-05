package com.example.munchies.ui.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.munchies.R
import com.example.munchies.databinding.ItemFeedBinding
import com.example.munchies.model.Review
import com.example.munchies.ui.review.ReviewDetailsActivity

class FeedAdapter (private val context: Context, val onLike : ((Review) -> Unit)  ) : ListAdapter<Review, FeedAdapter.ReviewViewHolder>(
    ReviewDiffCallback()
) {
    private lateinit var mContext: Context
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

            mContext = context

            binding.feedContent.text = review.caption
            binding.ratingBar.rating = review.rating.toFloat()
            binding.userName.text = review.user.username
            binding.restaurantName.text = review.restaurantName ?: "Unknown Restaurant"

            binding.root.setOnClickListener {
                val context = binding.root.context

                val intent = Intent(context, ReviewDetailsActivity::class.java).apply {
                    putExtra("review", review)
                }
                context.startActivity(intent)
            }
            if (!review.photos.isNullOrEmpty()) {
                val url = review.photos[0]
                Log.d("FeedAdapter", "Loading image from URL: $url")
                binding.feedImage.visibility = View.VISIBLE
                Glide.with(binding.feedImage.context)
                    .load(url)
                    .placeholder(R.drawable.sample_image) // show while loading
                    .error(R.drawable.sample_image)       // if URL fails
                    .into(binding.feedImage)
            } else {
                binding.feedImage.visibility = View.GONE
            }

        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.reviewID == newItem.reviewID
            //return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}
