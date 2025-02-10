package com.example.munchies.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "These are your friends!" +
                "You currently have none :("
    }
    val text: LiveData<String> = _text
}