package com.example.munchies.ui.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Friend
import com.example.munchies.model.User
import com.example.munchies.model.UserManager
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FriendsViewModel : ViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val repository = FriendRepository()
    private val userRepository = UserRepository()

    // Friend Profile Data
    private val _userName = MutableLiveData<String>()
    var userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    var userEmail: LiveData<String> = _userEmail

    private val _userBio = MutableLiveData<String>()
    var userBio: LiveData<String> = _userBio

    private val _userPfp = MutableLiveData<String?>()
    var userPfp: MutableLiveData<String?> = _userPfp

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    var error: LiveData<String?> = _error

    private val _text = MutableLiveData<String>().apply {
        value = "These are your friends! \nYou currently have none :("
    }
    
    val text: LiveData<String> = _text

    private val _friendsList = MutableLiveData<List<User>?>()
    val friendsList: MutableLiveData<List<User>?> = _friendsList

    private val _incomingRequestsList = MutableLiveData<List<User>?>()
    val incomingRequestsList: MutableLiveData<List<User>?> = _incomingRequestsList

    private val _outgoingRequestsList = MutableLiveData<List<User>?>()
    val outgoingRequestsList: MutableLiveData<List<User>?> = _outgoingRequestsList

    private val _userList = MutableLiveData<List<User>?>()
    val userList: MutableLiveData<List<User>?> = _userList

    private val _filteredFriendsList = MutableLiveData<List<User>?>()
    val filteredFriendsList: MutableLiveData<List<User>?> = _filteredFriendsList

    fun fetchFriends() {
        Log.d("FriendsViewModel", "Fetching friends for user: $userId")
        viewModelScope.launch {
            _isLoading.value = true
            if (userId != null) {
                repository.getUserFriends(
                    userId
                ) { response ->
                    if (response != null) {
                        _friendsList.value = response
                        _filteredFriendsList.value = response
                        UserManager.friends = response
                    } else {
                        Log.e("FriendsViewModel", "Failed to get friends")
                    }
                }
                repository.getIncomingFriendRequests(
                    userId
                ) { response ->
                    if (response != null) {
                        _incomingRequestsList.value = response
                        UserManager.incomingFriendRequests = response
                    } else {
                        Log.e("FriendsViewModel", "Failed to get incoming requests")
                    }
                }
                repository.getOutgoingFriendRequests(
                    userId
                ) { response ->
                    if (response != null) {
                        _outgoingRequestsList.value = response
                        UserManager.outgoingFriendRequests = response
                    } else {
                        Log.e("FriendsViewModel", "Failed to get outgoing requests")
                    }
                }

            }
            _isLoading.value = false
            Log.d("FriendsViewModel", "Friends fetched successfully")
        }
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getAllUsers { response ->
                if (response != null) {
                    Log.d("FriendsViewModel", "Get all users")
                    _userList.value = response
                } else {
                    Log.e("FriendsViewModel", "Failed to get all users")
                }
            }
            _isLoading.value = false

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

    fun loadUserData(userId: String) {
        Log.d("FriendsViewModel", "Loading user data for Firebase UID: ${userId}")

        viewModelScope.launch {
            _isLoading.value = true
            userRepository.getUserById(
                userId
            ) { response: User? ->
                run {
                    Log.d("FriendsViewModel", "User data received: $response")
                    _userName.value = response?.name
                    _userEmail.value = response?.emailAddress
                    _userBio.value = response?.userBio ?: "No bio yet"
                    _userPfp.value = response?.profilePicture
                }
            }
            _isLoading.value = false
        }
    }

    fun addFriend(friendId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (userId != null) {
                repository.addFriend(
                    userId, friendId
                ) { response ->
                    if (response == null) {
                        Log.d("FriendsViewModel", "Friend request sent successfully: $response")
                        fetchFriends()
                    } else {
                        Log.e("FriendsViewModel", "Failed to send friend request")
                    }
                }
            }
        }
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (userId != null) {
                repository.deleteFriend(
                    userId, friendId
                ) { response ->
                    if (response == null) {
                        fetchFriends()
                    } else {
                        Log.e("FriendsViewModel", "Failed to delete friend")
                    }
                }
            }
        }
    }

    fun acceptFriendRequest(friendId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if (userId != null) {
                repository.acceptFriendRequest(
                    userId, friendId
                ) { response ->
                    if (response == null) {
                        fetchFriends()
                        Log.d("FriendsViewModel", "Friend request accepted successfully: $response")
                    } else {
                        Log.e("FriendsViewModel", "Failed to accept friend request")
                    }
                }
            }
        }
    }

    fun isFriend(friendId: String): Boolean {
        Log.d("FriendsViewModel", "Checking if $friendId is a friend of $userId")
        val res = UserManager.friends?.any { it.id == friendId } == true
        Log.d("FriendsViewModel", "Result: $res")
        return res
    }

    fun isIncomingFriendRequest(friendId: String): Boolean {
        Log.d("FriendsViewModel", "Checking if $friendId has a pending friend request from $userId")
        val res = UserManager.incomingFriendRequests?.any { it.id == friendId } == true
        Log.d("FriendsViewModel", "Result: $res")
        return res
    }

    fun isOutgoingFriendRequest(friendId: String): Boolean {
        Log.d("FriendsViewModel", "Checking if $userId has a pending friend request to $friendId")
        val res = UserManager.outgoingFriendRequests?.any { it.id == friendId } == true
        Log.d("FriendsViewModel", "Result: $res")
        return res
    }

    init {
        fetchFriends()
        fetchAllUsers()
    }
}