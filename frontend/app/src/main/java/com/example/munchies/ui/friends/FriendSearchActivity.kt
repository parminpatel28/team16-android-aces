package com.example.munchies.ui.friends
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.munchies.R
import com.example.munchies.model.Friend
import com.example.munchies.ui.home.adapter.FriendAdapter
import java.time.Instant


class FriendSearchActivity: AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendAdapter: FriendAdapter
    private var friendList = mutableListOf<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        // Sample friend list
        friendList.add(Friend(1, "Alice Johnson", "alice_j", ""))
        friendList.add(Friend(2, "Bob Smith", "bob_s", ""))
        friendList.add(Friend(3, "Charlie Brown", "charlie_b", ""))
        friendList.add(Friend(4, "David Lee", "david_lee", ""))

        friendAdapter = FriendAdapter(friendList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = friendAdapter

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
    }
}