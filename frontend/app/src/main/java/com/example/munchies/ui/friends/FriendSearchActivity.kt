package com.example.munchies.ui.friends
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.model.User
import com.example.munchies.ui.home.adapter.FriendAdapter
import com.example.munchies.databinding.ActivityFriendSearchBinding
import com.example.munchies.databinding.ActivityReviewBinding


class FriendSearchActivity: AppCompatActivity() {
    private lateinit var _binding: ActivityFriendSearchBinding

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private var userList = mutableListOf<User>()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFriendSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)
        recyclerView = binding.recyclerView
        searchView = binding.searchView
        recyclerView.layoutManager = LinearLayoutManager(this)
        friendAdapter = FriendAdapter(userList, friendsViewModel, this)
        recyclerView.adapter = friendAdapter
        friendsViewModel.fetchAllUsers()
        recyclerView.visibility = View.GONE
        binding.noResults.visibility = View.VISIBLE
        binding.noResults.text = "Please enter at least 3 characters to search"

        // Back button
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Friends"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Observe the LiveData from the ViewModel
        friendsViewModel.userList.observe(this) { userList ->
            if (userList != null) {
                friendAdapter.updateList(userList)
            }  // Update the list in the RecyclerView
        }
        friendsViewModel.incomingRequestsList.observe(this) { userList ->
            if (userList != null) {
                friendAdapter.updateList(friendsViewModel.userList.value)
            }  // Update the list in the RecyclerView
        }
        friendsViewModel.outgoingRequestsList.observe(this) { userList ->
            if (userList != null) {
                friendAdapter.updateList(friendsViewModel.userList.value)
            }  // Update the list in the RecyclerView
        }

        // Implement search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Check if at least 3 characters have been entered
                if (newText == null || newText.length < 3) {
                    // Hide the list and show a message prompting for more characters
                    recyclerView.visibility = View.GONE
                    binding.noResults.visibility = View.VISIBLE
                    binding.noResults.text = "Please enter at least 3 characters to search"
                    // Optionally update adapter with an empty list
                } else {
                    // Show the list (in case it was hidden) and filter the results
                    recyclerView.visibility = View.VISIBLE
                    friendAdapter.filter.filter(newText) { count ->
                        if (count == 0) {
                            // If filtering returns no results, hide the list and show "no results" message
                            recyclerView.visibility = View.GONE
                            binding.noResults.visibility = View.VISIBLE
                            binding.noResults.text = "No results found"
                        } else {
                            // Otherwise, show the list and hide the empty message
                            recyclerView.visibility = View.VISIBLE
                            binding.noResults.visibility = View.GONE
                        }
                    }
                }
                return true
            }
        })

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Add Friends"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}