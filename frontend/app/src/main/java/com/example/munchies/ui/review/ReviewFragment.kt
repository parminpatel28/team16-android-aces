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
import android.util.Log
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import android.view.ViewGroup.MarginLayoutParams

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewViewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        // Adjust FAB to stay above the navigation bar dynamically
        ViewCompat.setOnApplyWindowInsetsListener(binding.addReviewButton) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = systemBars.bottom + 24 // Moves FAB up dynamically
            }
            insets
        }

        // ✅ Setup RecyclerView
        val recyclerView = binding.recyclerReviews
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ReviewAdapter()
        recyclerView.adapter = adapter

        reviewViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            adapter.submitList(reviewList)
        }

        // ✅ Launch ReviewActivity when the button is clicked
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
