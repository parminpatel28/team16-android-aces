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
import com.example.munchies.model.Location
import com.example.munchies.model.Review
import com.example.munchies.model.User
import com.example.munchies.repository.ReviewRepository
import java.time.Instant

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val reviewRepository = ReviewRepository()
    private val userId = 1  // TODO: Replace with actual logged-in user ID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchReviewsByUser(userId)

        binding.addReviewButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReviewActivity::class.java))
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ReviewAdapter { selectedReview ->
            val intent = Intent(requireContext(), ReviewDetailsActivity::class.java)
            intent.putExtra("review", selectedReview)
            startActivity(intent)
        }
        binding.recyclerReviews.layoutManager = LinearLayoutManager(context)
        binding.recyclerReviews.adapter = adapter
    }

    private fun fetchReviewsByUser(userId: Int) {
        reviewRepository.getReviewsByUser(userId) { reviews ->
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
