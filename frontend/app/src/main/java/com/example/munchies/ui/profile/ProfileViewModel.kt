package com.example.munchies.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.munchies.model.Review

class ProfileViewModel : ViewModel() {

    // User Profile Data
    private val _userName = MutableLiveData<String>().apply { value = "John Doe" }
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>().apply { value = "john.doe@example.com" }
    val userEmail: LiveData<String> = _userEmail

    private val _userReviews = MutableLiveData<List<Review>>().apply {value = listOf()}
    val userReviews: LiveData<List<Review>> = _userReviews

    private val _userFriends = MutableLiveData<List<Int>>().apply {value = listOf()}
    val userFriends: LiveData<List<Int>> = _userFriends

    private val _userBio = MutableLiveData<String>().apply {value = "I loooove eating food"}
    val userBio: LiveData<String> = _userBio

    private val _userPfp = MutableLiveData<String>().apply {value = "https://e.snmc.io/i/fullres/w/92a83a11be8d457d5fc32ac7477db0c3/11130567"}
    val userPfp: LiveData<String> = _userPfp

    // Handle logout logic
    fun logout() {
        // Handle any necessary logout operations (clear session, tokens, etc.)
        // For now, we'll just print a message and clear the data for demonstration.
        _userName.value = "Logged out"
        _userEmail.value = "No email available"
        _userBio.value = "..."
    }
}
