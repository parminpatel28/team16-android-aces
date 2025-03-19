package com.example.munchies.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Friend
import com.example.munchies.repository.FriendRepository
import kotlinx.coroutines.launch
import java.time.Instant

class FriendsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "These are your friends! \nYou currently have none :("
    }
    val text: LiveData<String> = _text

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: LiveData<List<Friend>> = _friendsList

    private val _filteredFriendsList = MutableLiveData<List<Friend>>()
    val filteredFriendsList: LiveData<List<Friend>> = _filteredFriendsList

    private val friendRepository = FriendRepository()
    // Simulated backend fetch
    private fun fetchFriends() {
        viewModelScope.launch {



            val data = listOf(
                Friend(userId = 1, username = "elaine", name = "Elaine", profilePicture = ""),
                Friend(userId = 2, username = "elaine", name = "Elaine", profilePicture = ""),
                Friend(userId = 3, username = "elaine", name = "Elaine", profilePicture = ""),
            )
            _friendsList.value = data
            _filteredFriendsList.value = data
        }
    }

    fun searchFriends(query: String) {
        val allFriends = _friendsList.value ?: return
        _filteredFriendsList.value = if (query.isEmpty()) {
            allFriends
        } else {
            allFriends.filter {
                it.username.contains(query, ignoreCase = true) ||
                        it.username.contains(query, ignoreCase = true)
            }
        }
    }

    init {
        fetchFriends()
    }
}