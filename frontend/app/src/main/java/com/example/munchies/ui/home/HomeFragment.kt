package com.example.munchies.ui.home

import android.os.Bundle
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
    private lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calledByViewReviews = arguments?.getBoolean("fromMap")
        val placeAdress = arguments?.getString("address")

        if (calledByViewReviews == true) {
            binding.returnButtonFeed.isEnabled = true
            binding.returnButtonFeed.visibility = VISIBLE
        } else {
            binding.returnButtonFeed.isEnabled = false
            binding.returnButtonFeed.visibility = INVISIBLE
        }
        binding.returnButtonFeed.setOnClickListener {
            binding.returnButtonFeed.isEnabled = false
            binding.returnButtonFeed.visibility = INVISIBLE
            activity?.finish()
        }

        // Set up RecyclerView with FeedAdapter
        val recyclerView = binding.recyclerFeed
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FeedAdapter(requireContext()){ review: Review -> run{
            homeViewModel.likeReview(review)

        }


        }
        recyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        homeViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            adapter.submitList(reviewList)  // Update the list in the RecyclerView
        }


        homeViewModel.refresh.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        setupSwipeToRefresh()

        return root
    }

    private fun populateFeed() {

        adapter.submitList(null)
        // Observe the LiveData from the ViewModel
        homeViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            adapter.submitList(reviewList)  // Update the list in the RecyclerView
        }


    }
    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshFeed()
            //populateFeed()

        }
    }

    fun newInstance(address : String?, fromMap: Boolean): HomeFragment {
        val fragment = HomeFragment()

        val bundle = Bundle().apply {
            putString("address", address)
            putBoolean("fromMap", fromMap)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.refreshFeed()
        //populateFeed()
    }


}
