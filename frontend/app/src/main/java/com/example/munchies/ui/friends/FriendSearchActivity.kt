package com.example.munchies.ui.friends
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
                friendAdapter.filter.filter(newText)
                return true
            }
        })

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = "Add Friends"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}