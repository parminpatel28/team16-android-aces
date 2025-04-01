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
import com.google.android.material.tabs.TabLayout

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private var friendList = mutableListOf<User>()
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter

    private var currentTab = 0


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

        val tabLayout = binding.friendsTabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                when (currentTab) {
                    0 -> {
                        Log.d("FriendsFragment", "Friends tab selected, updating FriendAdapter")
                        friendsViewModel.friendsList.value?.let { friendAdapter.updateList(it) }
                    }
                    1 -> {
                        Log.d("FriendsFragment", "Requests tab selected, updating FriendAdapter")
                        friendsViewModel.incomingRequestsList.value?.let { friendAdapter.updateList(it) }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Initialize adapter with friends list (default tab is Friends)

        friendsViewModel.friendsList.value?.let {
            Log.d("FriendsFragment", "Initializing adapter with friends list")
            friendAdapter.updateList(it)
        }

        // Observe friends list
        friendsViewModel.friendsList.observe(viewLifecycleOwner) { friendList ->
            // Update only if the Friends tab is active
            if (currentTab == 0 && friendList != null) {
                Log.d("FriendsFragment", "Current Tab: $currentTab, Friend list updated: $friendList")
                friendAdapter.updateList(friendList)
                binding.noFriendRequests.visibility = View.GONE
                binding.noFriends.visibility = if (friendList.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Observe incoming friend requests
        friendsViewModel.incomingRequestsList.observe(viewLifecycleOwner) { friendList ->
            // Update only if the Requests tab is active
            if (currentTab == 1 && friendList != null) {
                Log.d("FriendsFragment", "Current Tab: $currentTab, Incoming requests updating: $friendList")
                friendAdapter.updateList(friendList)
                binding.noFriendRequests.visibility = if (friendList.isEmpty()) View.VISIBLE else View.GONE
                binding.noFriends.visibility = View.GONE
            }
        }

        btnSearchFriends.setOnClickListener {
            startActivity(Intent(context, FriendSearchActivity::class.java))
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