package com.example.munchies.ui.home.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.databinding.ItemFriendBinding
import android.widget.Filter
import com.example.munchies.model.User
import com.example.munchies.ui.friends.FriendDetailActivity

class FriendAdapter(private var friendList: MutableList<User>) :
    ListAdapter<User, FriendAdapter.FriendViewHolder>(FriendDiffCallback()), Filterable {
//    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>(), Filterable {
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
            friendList = newList as MutableList<User>
        }
        if (newList != null) {
            filteredFriendList = newList
        }
        notifyDataSetChanged()
    }

    inner class FriendViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: User) {
            binding.friendProfilePicture.contentDescription = friend.profilePicture
            binding.friendname.text = friend.name
            binding.friendUserName.text = friend.username

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent =
                    Intent(context, FriendDetailActivity::class.java).apply {
                    putExtra("friend_id", friend.id)
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
}
