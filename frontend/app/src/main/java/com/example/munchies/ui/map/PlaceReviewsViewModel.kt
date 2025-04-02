package com.example.munchies.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository
import kotlinx.coroutines.launch

class PlaceReviewsViewModel(private val repository: ReviewRepository) : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    fun fetchReviewsForPlace(placeID: String) {
        viewModelScope.launch {
            val result = repository.getReviewsByRestaurantId(placeID)
            result?.let {
                _reviews.postValue(it)
            }
        }
    }
}
