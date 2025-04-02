package com.example.munchies.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.R
import com.example.munchies.databinding.FragmentPlaceReviewsBinding
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository
import com.example.munchies.ui.review.ReviewAdapter
import com.example.munchies.ui.review.ReviewDetailsActivity
import com.google.android.libraries.places.api.model.kotlin.place

class PlaceReviewsFragment : Fragment() {

    private var _binding: FragmentPlaceReviewsBinding? = null
    private val binding get() = _binding!!

    private var placeID: String? = null
    private lateinit var viewModel: PlaceReviewsViewModel
    private lateinit var adapter: ReviewAdapter
    private val repository = ReviewRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeID = arguments?.getString("placeID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.topAppBar
        toolbar.title = arguments?.getString("placeName") ?: "Restaurant Reviews"
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = ReviewAdapter { review: Review ->
            val intent = Intent(requireContext(), ReviewDetailsActivity::class.java)
            intent.putExtra("review", review)
            startActivity(intent)
        }

        binding.recyclerFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFeed.adapter = adapter

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PlaceReviewsViewModel(repository) as T
            }
        })[PlaceReviewsViewModel::class.java]

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.submitList(reviews)
        }

        placeID?.let {
            viewModel.fetchReviewsForPlace(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(placeID: String): PlaceReviewsFragment {
            val fragment = PlaceReviewsFragment()
            val args = Bundle().apply {
                putString("placeID", placeID)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
