package com.example.munchies.ui.friends

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.databinding.FragmentFriendsBinding
import com.example.munchies.model.User
import com.example.munchies.ui.home.adapter.FriendAdapter

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private var friendList = mutableListOf<User>()
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)
        friendsViewModel.fetchFriends()
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.recyclerFriends
        searchView = binding.searchFriends
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        friendAdapter = FriendAdapter(friendList, friendsViewModel, viewLifecycleOwner)
        recyclerView.adapter = friendAdapter

        binding.recyclerFriends.layoutManager = LinearLayoutManager(context)
        binding.recyclerFriends.adapter = friendAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            friendsViewModel.fetchFriends()
        }

        val btnSearchFriends = binding.btnAddFriends
        val btnFriendRequests = binding.btnFriendRequests

        // Observe the LiveData from the ViewModel
        friendsViewModel.friendsList.observe(viewLifecycleOwner) { friendList ->
            Log.d("FriendsFragment", "Friend list updated: $friendList")
            if (friendList != null) {
                friendAdapter.updateList(friendList)
            }  // Update the list in the RecyclerView
        }

        btnSearchFriends.setOnClickListener {
            startActivity(Intent(context, FriendSearchActivity::class.java))
        }
        btnFriendRequests.setOnClickListener {
            startActivity(Intent(context, FriendRequestsActivity::class.java))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                friendAdapter.filter.filter(newText)
                return true
            }
        })


        return root
    }
}