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
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.addReviewButton) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = systemBars.bottom + 24 // Moves FAB up dynamically
            }
            insets
        }

        reviewAdapter = ReviewAdapter { selectedReview ->
            val intent = Intent(requireContext(), ReviewDetailsActivity::class.java)
            intent.putExtra("review", selectedReview)
            startActivity(intent)
        }

        val recyclerView = binding.recyclerReviews
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = reviewAdapter

        reviewViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            reviewAdapter.submitList(reviewList)
        }

        binding.addReviewButton.setOnClickListener {
            val intent = Intent(requireContext(), com.example.munchies.ui.review.ReviewActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
