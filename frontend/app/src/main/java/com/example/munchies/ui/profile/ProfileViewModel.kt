package com.example.munchies.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // User Profile Data
    private val _userName = MutableLiveData<String>().apply { value = "John Doe" }
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>().apply { value = "john.doe@example.com" }
    val userEmail: LiveData<String> = _userEmail

    // Handle logout logic
    fun logout() {
        // Handle any necessary logout operations (clear session, tokens, etc.)
        // For now, we'll just print a message and clear the data for demonstration.
        _userName.value = "Logged out"
        _userEmail.value = "No email available"
    }
}
