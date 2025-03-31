package com.example.munchies.ui.friends
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.munchies.R
import com.example.munchies.databinding.ActivityFriendDetailBinding
import com.example.munchies.model.User

class FriendDetailActivity() : AppCompatActivity() {
    private lateinit var _binding: ActivityFriendDetailBinding
    private lateinit var friendsViewModel: FriendsViewModel

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFriendDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        val context = this

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Friend Profile"

        // Handle back button click
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val id = intent.getStringExtra("friend_id") ?: "Unknown"
        val name = intent.getStringExtra("friend_name") ?: "Unknown"
        val username = intent.getStringExtra("friend_username") ?: "Unknown"
        val email = intent.getStringExtra("friend_email") ?: "Unknown"
        val bio = intent.getStringExtra("friend_bio") ?: "Unknown"
        val pfp = intent.getStringExtra("friend_pfp") ?: "Unknown"
        friendsViewModel.loadUserData(id)
        binding.textProfileName.text = name
        binding.textProfileEmail.text = email
        binding.textProfileBio.text = bio

        if (friendsViewModel.isFriend(id)) {
            Log.d("FriendDetailActivity", "Friend is already a friend")
            binding.addFriendButton.visibility = View.GONE
            binding.removeFriendButton.visibility = View.VISIBLE
            binding.removeFriendButton.setOnClickListener {
                friendsViewModel.deleteFriend(id)
            }
        } else if (friendsViewModel.isOutgoingFriendRequest(id)) {
            Log.d("FriendDetailActivity", "Friend has a pending friend request")
            binding.addFriendButton.visibility = View.GONE
            binding.removeFriendButton.visibility = View.VISIBLE
            binding.removeFriendButton.text = context.getString(R.string.cancel_request)
            binding.removeFriendButton.setOnClickListener {
                friendsViewModel.deleteFriend(id)
            }
        } else if (friendsViewModel.isIncomingFriendRequest(id)) {
            Log.d("FriendDetailActivity", "Friend has a pending friend request")
            binding.addFriendButton.visibility = View.VISIBLE
            binding.removeFriendButton.visibility = View.VISIBLE
            binding.addFriendButton.text = context.getString(R.string.accept_request)
            binding.addFriendButton.setOnClickListener {
                friendsViewModel.acceptFriendRequest(id)
            }
            binding.removeFriendButton.text = context.getString(R.string.reject_request)
            binding.removeFriendButton.setOnClickListener {
                friendsViewModel.deleteFriend(id)
            }
        } else {
            Log.d("FriendDetailActivity", "Friend is not a friend")
            binding.addFriendButton.visibility = View.VISIBLE
            binding.removeFriendButton.visibility = View.GONE
            binding.addFriendButton.setOnClickListener {
                friendsViewModel.addFriend(id)
            }
        }
        if (pfp.isNotEmpty()) {
            Glide.with(applicationContext)
                .load(pfp)
                .into(binding.imageProfilePicture)
        }

    }

//    private fun setupObservers() {
//        friendsViewModel.userName.observe(this) {
//            binding.textProfileName.text = it
//        }
//
//        friendsViewModel.userEmail.observe(this) {
//            binding.textProfileEmail.text = it
//        }
//
//        friendsViewModel.userBio.observe(this) {
//            binding.textProfileBio.text = it
//            binding.textInputProfileBio.hint = it
//        }
//
//        friendsViewModel.userPfp.observe(this) { url ->
//            if (!url.isNullOrEmpty()) {
//                Glide.with(applicationContext)
//                    .load(url)
//                    .into(binding.imageProfilePicture)
//            }
//        }
//
//        friendsViewModel.isLoading.observe(this) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            binding.friendProfileContent.visibility = if (isLoading) View.GONE else View.VISIBLE
//        }
//
//        friendsViewModel.error.observe(this) { error ->
//            error?.let {
//                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun setupClickListeners() {
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            friendsViewModel.loadUserData(this.id)
//            binding.swipeRefreshLayout.isRefreshing = false
//        }
//    }
}
