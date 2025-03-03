package com.example.munchies.ui.review

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.databinding.FragmentReviewBinding
import com.example.munchies.model.Review
import java.time.Instant

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        setupRecyclerView()
        loadMockReviews()

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

    private fun loadMockReviews() {
        val mockReviews = listOf(
            Review(1, "Taylor", "Great place!", location = "Waterloo", date = Instant.now(), rating = 4.5, restaurants = listOf("Taco Bell")),
            Review(2, "Taylor", "Loved the sushi!", location = "Waterloo", date = Instant.now(), rating = 4.0, restaurants = listOf("Ye's Sushi")),
            Review(3, "Taylor", "Not bad", location = "Waterloo", date = Instant.now(), rating = 2.5, restaurants = listOf("Burger King"))
        )
        adapter.submitList(mockReviews) // TEMPORARY until backend is ready
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
