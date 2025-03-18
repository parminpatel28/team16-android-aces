package com.example.munchies.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.User
import com.example.munchies.api.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("ProfileViewModel", "OkHttp: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Android emulator localhost
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userService = retrofit.create(UserService::class.java)
        loadUserData()
    }

    private fun loadUserData() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            Log.e("ProfileViewModel", "No Firebase user found")
            _error.value = "User not logged in"
            return
        }

        Log.d("ProfileViewModel", "Loading user data for Firebase UID: ${firebaseUser.uid}")

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userService.getUserById(firebaseUser.uid)
                Log.d("ProfileViewModel", "Response code: ${response.code()}")
                
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        Log.d("ProfileViewModel", "User data received: $user")
                        _userName.value = user.name
                        _userEmail.value = user.emailAddress
                        _userBio.value = user.userBio ?: "No bio yet"
                        _userPfp.value = user.profilePicture
                    } ?: run {
                        Log.e("ProfileViewModel", "Response body is null")
                        _error.value = "User data is null"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileViewModel", "Error response: $errorBody")
                    _error.value = "Failed to load user data: $errorBody"
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Exception loading user data", e)
                _error.value = "Error loading user data: ${e.message}"
                e.printStackTrace()
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