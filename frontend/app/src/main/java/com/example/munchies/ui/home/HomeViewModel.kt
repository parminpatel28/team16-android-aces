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
        viewModelScope.launch {
            // Replace with actual backend call
            val data = listOf(
                Review(1, "Friend #1", "The junior chicken is rly good!", emptyList(), emptyList(), listOf("McDonalds"), null, Instant.now(), 5.0, 12),
                Review(2, "Friend #2", "I <3 lazeez", emptyList(), emptyList(), listOf("Lazeez Shawarma"), null, Instant.now(), 4.5, 8),
                Review(3, "Friend #3", "5 stars for the shrimp tacos", emptyList(), emptyList(), listOf("Los Rolling Tacos"), null, Instant.now(), 5.0, 15),
                Review(4, "Friend #4", "Just tried Nuri Village... YUM!!", emptyList(), emptyList(), listOf("Nuri Village"), null, Instant.now(), 4.5, 20),
                Review(5, "Friend #5", "Highly recommend Kabob Hut!", emptyList(), emptyList(), listOf("Kabob Hut"), null, Instant.now(), 5.0, 10),
                Review(6, "Friend #6", "DO NOT TRY THE TACO BELL SUSHI XXX", emptyList(), emptyList(), listOf("Taco Bell"), null, Instant.now(), 0.5, 3)
            )
            _reviews.value = data
        }
    }

    init {
        fetchReviews()
    }
}
