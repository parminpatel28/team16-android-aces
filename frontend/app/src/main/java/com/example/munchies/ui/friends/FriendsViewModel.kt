package com.example.munchies.ui.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Friend
import com.example.munchies.model.User
import com.example.munchies.repository.FriendRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.Instant

class FriendsViewModel : ViewModel() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val repository = FriendRepository()

    private val _text = MutableLiveData<String>().apply {
        value = "These are your friends! \nYou currently have none :("
    }
    val text: LiveData<String> = _text

    private val _friendsList = MutableLiveData<List<User>?>()
    val friendsList: MutableLiveData<List<User>?> = _friendsList

    private val _filteredFriendsList = MutableLiveData<List<User>?>()
    val filteredFriendsList: MutableLiveData<List<User>?> = _filteredFriendsList

    private fun fetchFriends() {
        viewModelScope.launch {
            if (userId != null) {
                repository.getUserFriends(
                    userId
                ) { response ->
                    if (response != null) {
                        _friendsList.value = response
                        _filteredFriendsList.value = response
                    } else {
                        Log.e("FriendsViewModel", "Failed to get friends")
                    }
                }

            }

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