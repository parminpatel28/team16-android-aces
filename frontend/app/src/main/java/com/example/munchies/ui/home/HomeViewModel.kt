package com.example.munchies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Review
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    // Simulate fetching data from backend
    fun fetchReviews() {
        viewModelScope.launch {
            // Replace with your backend call
            val data = listOf(
                Review(1, "Friend #1", "McDonalds", "The junior chicken is rly good!"),
                Review(2, "Friend #2", "Lazeez Shawarma", "I <3 lazeez"),
                Review(3, "Friend #3", "Los Rolling Tacos", "5 stars for the shrimp tacos"),
                Review(4, "Friend #4", "Nuri Village", "Just tried Nuri Village... YUM!!"),
                Review(5, "Friend #5", "Kabob Hut", "Highly recommend Kabob Hut!"),
                Review(6, "Friend #6", "Taco Bell", "DO NOT TRY THE TACO BELL SUSHI XXX")
            )
            _reviews.value = data
        }
    }

    init {
        fetchReviews()
    }
}
