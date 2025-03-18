package com.example.munchies.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.User
import com.example.munchies.api.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModel : ViewModel() {

    // User Profile Data


    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _userReviews = MutableLiveData<List<String>>(emptyList())
    val userReviews: LiveData<List<String>> = _userReviews

    private val _userFriends = MutableLiveData<List<String>>(emptyList())
    val userFriends: LiveData<List<String>> = _userFriends

    private val _userBio = MutableLiveData<String>()
    val userBio: LiveData<String> = _userBio

    private val _userPfp = MutableLiveData<String>()
    val userPfp: LiveData<String> = _userPfp

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val userService: UserService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Android emulator localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userService = retrofit.create(UserService::class.java)
        loadUserData()
    }

    private fun loadUserData() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _error.value = "User not logged in"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userService.getUserById(firebaseUser.uid)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _userName.value = user.name
                        _userEmail.value = user.emailAddress
                        _userBio.value = user.userBio ?: "No bio yet"
                        _userPfp.value = user.profilePicture
                    }
                } else {
                    _error.value = "Failed to load user data: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading user data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Handle logout logic
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun refreshUserData() {
        loadUserData()
    }
}