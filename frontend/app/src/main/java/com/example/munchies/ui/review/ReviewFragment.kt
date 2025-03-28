package com.example.munchies.ui.review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.databinding.FragmentReviewBinding
import com.example.munchies.model.UserManager
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.ReviewRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewAdapter
    private val reviewRepository = ReviewRepository()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeToRefresh()

        binding.addReviewButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReviewActivity::class.java))
        }

        userId?.let {
            Log.d("ReviewFragment", "UserId: $it")
            fetchReviewsByUser(it)
        }
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
            if (userId != null) {
                fetchReviewsByUser(userId)
            }  // Refresh reviews
        }
    }

    private fun fetchReviewsByUser(userId: String) {
        binding.swipeRefreshLayout.isRefreshing = true

        viewLifecycleOwner.lifecycleScope.launch {
            val reviews = withContext(Dispatchers.IO) {
                reviewRepository.getReviewsByUser(userId)
            }

            binding.swipeRefreshLayout.isRefreshing = false
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
