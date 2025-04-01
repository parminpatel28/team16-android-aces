package com.example.munchies.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.databinding.FragmentHomeBinding
import com.example.munchies.model.Review
import com.example.munchies.ui.home.adapter.FeedAdapter
import com.example.munchies.ui.map.MapFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* placeID */
        val calledByViewReviews = arguments?.getBoolean("fromMap")
        val placeID = arguments?.getString("placeID")

        Log.d("HomeFragment", placeID.toString())
        /* placeID */

        // Check if the activity was called from the map
        Log.d("HomeFragment", calledByViewReviews.toString())
        if (calledByViewReviews != null && calledByViewReviews == true) {
            binding.returnButtonFeed.isEnabled = true
            binding.returnButtonFeed.visibility = VISIBLE
            /* placeID */
            homeViewModel.setFromMap(true)
            homeViewModel.setPlaceID(placeID!!)
            /* placeID */
        } else {
            binding.returnButtonFeed.isEnabled = false
            binding.returnButtonFeed.visibility = INVISIBLE

            /* placeID */
            homeViewModel.setFromMap(false)
            /* placeID */
        }
        binding.returnButtonFeed.setOnClickListener {
            binding.returnButtonFeed.isEnabled = false
            binding.returnButtonFeed.visibility = INVISIBLE
            activity?.finish()
        }

        // Set up RecyclerView with FeedAdapter
        val recyclerView = binding.recyclerFeed
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = FeedAdapter{ review: Review -> run{
            homeViewModel.likeReview(review)

        }


        }
        recyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        homeViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            adapter.submitList(reviewList)  // Update the list in the RecyclerView

            if (reviewList.isNullOrEmpty()) {
                binding.recyclerFeed.visibility = View.GONE
                binding.textNoFeed.visibility = View.VISIBLE
            } else {
                binding.recyclerFeed.visibility = View.VISIBLE
                binding.textNoFeed.visibility = View.GONE
            }
        }

        homeViewModel.refresh.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        setupSwipeToRefresh()

        return root
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshFeed()
        }
    }

    fun newInstance(placeID : String?, fromMap: Boolean): HomeFragment {
        val fragment = HomeFragment()

        val bundle = Bundle().apply {
            /* placeID */
            putString("placeID", placeID)
            /* placeID */
            putBoolean("fromMap", fromMap)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
