package com.example.munchies.ui.review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.databinding.FragmentReviewBinding
import com.example.munchies.repository.ReviewRepository

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val reviewRepository = ReviewRepository()
    private val userId = 1  // TODO: Replace with actual logged-in user ID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSwipeToRefresh()
        fetchReviewsByUser(userId)  // Fetch reviews when fragment loads

        binding.addReviewButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReviewActivity::class.java))
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ReviewAdapter { selectedReview ->
            Log.d("Review Clicked", "Review: $selectedReview")
            Log.d("Review Clicked", "User: ${selectedReview.user}")
            val intent = Intent(requireContext(), ReviewDetailsActivity::class.java)
            intent.putExtra("review", selectedReview)
            startActivity(intent)
        }
        binding.recyclerReviews.layoutManager = LinearLayoutManager(context)
        binding.recyclerReviews.adapter = adapter
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchReviewsByUser(userId)  // Refresh reviews
        }
    }

    private fun fetchReviewsByUser(userId: Int) {
        binding.swipeRefreshLayout.isRefreshing = true // Show refresh indicator

        reviewRepository.getReviewsByUser(userId) { reviews ->
            binding.swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
            if (reviews != null) {
                adapter.submitList(reviews)
            } else {
                Log.e("ReviewFragment", "Failed to fetch reviews")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
