package com.example.munchies.ui.home.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.databinding.ItemFriendBinding
import com.example.munchies.model.User
import com.example.munchies.ui.friends.FriendDetailActivity
import com.example.munchies.ui.friends.FriendsViewModel

class FriendAdapter(private var friendList: MutableList<User>,
                    private val viewModel: FriendsViewModel,
lifecycleOwner: LifecycleOwner
):
    ListAdapter<User, FriendAdapter.FriendViewHolder>(FriendDiffCallback()), Filterable {

    var filteredFriendList: List<User> = friendList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = filteredFriendList[position]
        holder.bind(friend)
    }

    override fun getItemCount(): Int {
        return filteredFriendList.size
    }

    fun updateList(newList: List<User>?) {
        if (newList != null) {
            friendList = newList.toMutableList()
            filteredFriendList = newList
            notifyDataSetChanged()
        }
    }

    inner class FriendViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: User) {
            val context = binding.root.context
            binding.friendProfilePicture.contentDescription = friend.profilePicture
            binding.friendname.text = friend.name
            binding.friendUserName.text = friend.username
            if (viewModel.isFriend(friend.id)) {
                Log.d("FriendAdapter", "Friend is already a friend")
                binding.addFriendButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.removeFriendButton.setOnClickListener {
                    viewModel.deleteFriend(friend.id)
                }
            } else if (viewModel.isOutgoingFriendRequest(friend.id)) {
                Log.d("FriendAdapter", "Friend has a pending friend request")
                binding.addFriendButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.removeFriendButton.text = context.getString(R.string.cancel_request)
                binding.removeFriendButton.setOnClickListener {
                    viewModel.deleteFriend(friend.id)
                }
            } else if (viewModel.isIncomingFriendRequest(friend.id)) {
                Log.d("FriendAdapter", "Friend has a pending friend request")
                binding.addFriendButton.visibility = View.VISIBLE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.addFriendButton.text = context.getString(R.string.accept_request)
                binding.addFriendButton.setOnClickListener {
                    viewModel.acceptFriendRequest(friend.id)
                }
                binding.removeFriendButton.text = context.getString(R.string.reject_request)
                binding.removeFriendButton.setOnClickListener {
                    viewModel.deleteFriend(friend.id)
                }
            } else {
                Log.d("FriendAdapter", "Friend is not a friend")
                binding.addFriendButton.visibility = View.VISIBLE
                binding.removeFriendButton.visibility = View.GONE
                binding.addFriendButton.setOnClickListener {
                    viewModel.addFriend(friend.id)
                }
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent =
                    Intent(context, FriendDetailActivity()::class.java).apply {
                        putExtra("friend_id", friend.id)
                        putExtra("friend_name", friend.name)
                        putExtra("friend_username", friend.username)
                        putExtra("friend_email", friend.emailAddress)
                        putExtra("friend_bio", friend.userBio)
                        putExtra("friend_pfp", friend.profilePicture)
                    }
                context.startActivity(intent)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().lowercase()
                filteredFriendList = if (query.isEmpty()) {
                    friendList
                } else {
                    friendList.filter {
                        it.name.lowercase().contains(query) || it.username.lowercase().contains(query)
                    }
                }
                val results = FilterResults()
                results.values = filteredFriendList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFriendList = results?.values as List<User>
                notifyDataSetChanged()
            }
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    init {
//        // Observe changes from the view model and update the adapter when the friend list changes.
//        viewModel.friendsList.observe(lifecycleOwner) { newList ->
//            updateList(newList)
//        }
//        viewModel.userList.observe(lifecycleOwner) { newList ->
//            updateList(newList)
//        }
    }
}
