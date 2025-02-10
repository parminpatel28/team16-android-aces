package com.example.munchies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.ui.home.model.FeedItem
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _feedItems = MutableLiveData<List<FeedItem>>()
    val feedItems: LiveData<List<FeedItem>> = _feedItems

    // Simulate fetching data from backend
    fun fetchFeed() {
        viewModelScope.launch {
            // Replace with your backend call
            val data = listOf(
                FeedItem(1, "Friend #1", "The junior chicken from McDonalds is rly good!"),
                FeedItem(2, "Friend #2", "I <3 lazeez"),
                FeedItem(3, "Friend #3", "5 stars for Los Rolling Tacos"),
                FeedItem(4, "Friend #4", "Just tried Nuri Village... YUM!!"),
                FeedItem(5, "Friend #5", "Highly recommend Kabob Hut!"),
                FeedItem(6, "Friend #6", "DO NOT TRY THE TACO BELL SUSHI XXX")
            )
            _feedItems.value = data
        }
    }

    init {
        fetchFeed()
    }
}
