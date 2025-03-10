package com.example.munchies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Review
import kotlinx.coroutines.launch
import java.time.Instant

class HomeViewModel : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    // Simulated backend fetch
    fun fetchReviews() {
    }

    init {
        fetchReviews()
    }
}
