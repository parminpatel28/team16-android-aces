package com.example.munchies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.api.RetrofitClient
import com.example.munchies.model.UserProfile
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileBuilderViewModel : ViewModel() {

    // LiveData to notify the fragment of success or failure
    private val _profileUpdateSuccess = MutableLiveData<Boolean>()
    val profileUpdateSuccess: LiveData<Boolean> get() = _profileUpdateSuccess

    // Create a user profile in the backend
    fun createUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            try {
                // Call the API to create the user profile
                val response = RetrofitClient.apiService.createUserProfile(userProfile)
                handleResponse(response)
            } catch (e: Exception) {
                // If the API call fails, post false for failure
                _profileUpdateSuccess.postValue(false)
            }
        }
    }

    // Handle API response
    private fun handleResponse(response: Response<UserProfile>) {
        if (response.isSuccessful && response.body() != null) {
            // If the response is successful, post true for success
            _profileUpdateSuccess.postValue(true)
        } else {
            // If not successful, post false for failure
            _profileUpdateSuccess.postValue(false)
        }
    }
}
