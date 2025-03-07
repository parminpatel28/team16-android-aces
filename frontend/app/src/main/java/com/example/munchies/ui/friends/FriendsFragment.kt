package com.example.munchies.ui.friends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.databinding.FragmentFriendsBinding
import com.example.munchies.model.Friend
import com.example.munchies.ui.home.adapter.FriendAdapter
import java.time.Instant

class FriendsFragment : Fragment() {

private var _binding: FragmentFriendsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
    private var friendList = mutableListOf<Friend>()
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

    _binding = FragmentFriendsBinding.inflate(inflater, container, false)
    val root: View = binding.root

      // Set up RecyclerView with FeedAdapter
      recyclerView = binding.recyclerFriends
      searchView = binding.searchFriends
      recyclerView.layoutManager = LinearLayoutManager(requireContext())
      friendAdapter = FriendAdapter(friendList)
      val btnSearchFriends = binding.btnAddFriends
      recyclerView.adapter = friendAdapter

      // Observe the LiveData from the ViewModel
      friendsViewModel.friendsList.observe(viewLifecycleOwner) { friendList ->
          friendAdapter.updateList(friendList)  // Update the list in the RecyclerView
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