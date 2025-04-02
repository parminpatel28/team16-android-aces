package com.example.munchies.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.databinding.ItemReviewBinding
import com.example.munchies.model.Review
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReviewAdapter(private val onItemClick: (Review) -> Unit) :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
        holder.itemView.setOnClickListener { onItemClick(review) }
    }

    class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        private fun formatDate(instant: String): String {
            return try {
                val parsedInstant = Instant.parse(instant)
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                    .withLocale(Locale.getDefault())
                    .withZone(ZoneId.systemDefault())
                formatter.format(parsedInstant)
            } catch (e: Exception) {
                instant
            }
        }

        fun bind(review: Review) {
            binding.reviewText.text = review.caption
            binding.ratingBar.rating = review.rating.toFloat()
            binding.dateText.text = formatDate(review.date)
            binding.restaurantName.text = review.restaurantName ?: "Unknown Restaurant"
        }
    }

    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem.reviewID == newItem.reviewID
        override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
    }
}
