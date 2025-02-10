package com.example.munchies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.databinding.FragmentHomeBinding
import com.example.munchies.ui.home.adapter.FeedAdapter

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

        // Set up RecyclerView with FeedAdapter
        val recyclerView = binding.recyclerFeed
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = FeedAdapter()
        recyclerView.adapter = adapter

        // Observe the LiveData from the ViewModel
        homeViewModel.reviews.observe(viewLifecycleOwner) { reviewList ->
            adapter.submitList(reviewList)  // Update the list in the RecyclerView
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
