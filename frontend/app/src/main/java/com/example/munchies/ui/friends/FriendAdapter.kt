package com.example.munchies.ui.friends

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.databinding.ItemFriendBinding
import com.example.munchies.model.User


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
        Log.d("FriendAdapter", "Updating friend list: $newList")
        if (newList != null) {
            friendList = newList.toMutableList()
            filteredFriendList = newList
            notifyDataSetChanged()
        }
    }



    inner class FriendViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {

        private fun showRemoveFriendConfirmation(friend: User) {
            val context = binding.root.context
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove ${friend.username} from your friends?")
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.deleteFriend(friend.id)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }


        fun bind(friend: User) {
            val context = binding.root.context
            binding.friendProfilePicture.contentDescription = friend.profilePicture
            binding.friendname.text = friend.name
            binding.friendUserName.text = friend.username
            if (viewModel.isFriend(friend.id)) {
                Log.d("FriendAdapter", "${friend.username} is already a friend")
                binding.addFriendButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.GONE
                binding.optionsButton.visibility = View.VISIBLE

            } else if (viewModel.isOutgoingFriendRequest(friend.id)) {
                Log.d("FriendAdapter", "${friend.username} has a pending friend request")
                binding.addFriendButton.visibility = View.GONE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.GONE
                binding.removeFriendButton.text = context.getString(R.string.cancel_request)
                binding.removeFriendButton.setOnClickListener {
                    viewModel.deleteFriend(friend.id)
                }
            } else if (viewModel.isIncomingFriendRequest(friend.id)) {
                Log.d("FriendAdapter", "${friend.username} has a pending friend request")
                binding.addFriendButton.visibility = View.VISIBLE
                binding.removeFriendButton.visibility = View.VISIBLE
                binding.optionsButton.visibility = View.GONE
                binding.addFriendButton.text = context.getString(R.string.accept_request)
                binding.addFriendButton.setOnClickListener {
                    viewModel.acceptFriendRequest(friend.id)
                }
                binding.removeFriendButton.text = context.getString(R.string.reject_request)
                binding.removeFriendButton.setOnClickListener {
                    viewModel.deleteFriend(friend.id)
                }
            } else {
                Log.d("FriendAdapter", "${friend.username} is not a friend")
                binding.addFriendButton.visibility = View.VISIBLE
                binding.addFriendButton.text = context.getString(R.string.add_friend)
                binding.removeFriendButton.visibility = View.GONE
                binding.optionsButton.visibility = View.GONE
                binding.addFriendButton.setOnClickListener {
                    viewModel.addFriend(friend.id)
                }
            }

            binding.root.setOnClickListener {
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

            binding.optionsButton.setOnClickListener{ v ->
                val popup: PopupMenu = PopupMenu(v.context, v)
                popup.menuInflater.inflate(R.menu.item_friend_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.actionRemoveFriend -> {
                            showRemoveFriendConfirmation(friend)
                            true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                popup.show()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.d("FriendAdapter", "Filtering with constraint: $constraint")
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
                results.count = filteredFriendList.size
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
        // Observe changes from the view model and update the adapter when the friend list changes.
//        viewModel.friendsList.observe(lifecycleOwner) { newList ->
//            Log.d("FriendAdapter", "Friends list changed: $newList")
//            updateList(newList)
//        }
//        viewModel.userList.observe(lifecycleOwner) { newList ->
//            updateList(newList)
//        }
    }
}
