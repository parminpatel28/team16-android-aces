package com.example.munchies.ui.friends
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.munchies.databinding.ActivityFriendRequestsBinding


class FriendRequestsActivity: AppCompatActivity() {
    private lateinit var _binding: ActivityFriendRequestsBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private var userList = mutableListOf<User>()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFriendRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        friendAdapter = FriendAdapter(userList, friendsViewModel, this)
        recyclerView.adapter = friendAdapter
        friendsViewModel.fetchAllUsers()

        // Back button
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Friend Requests"

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Observe the LiveData from the ViewModel
        friendsViewModel.incomingRequestsList.observe(this) { userList ->
            if (userList != null) {
                friendAdapter.updateList(userList)
                if (userList.isEmpty()) {
                    binding.noFriendRequests.visibility = View.VISIBLE
                }
            }  // Update the list in the RecyclerView
        }

    }
}