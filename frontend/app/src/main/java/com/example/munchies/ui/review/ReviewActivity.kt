package com.example.munchies.ui.review

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityReviewBinding
import com.example.munchies.model.Review
import java.time.Instant
import android.widget.MultiAutoCompleteTextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()

    private var selectedPhotos: MutableList<String> = mutableListOf()
    private var taggedUsers: MutableList<String> = mutableListOf()
    private var taggedRestaurants: MutableList<String> = mutableListOf()
    private var selectedLocation: String? = null

    private fun setupUserTagging() {
        val friendsList = listOf("Taylor", "Elaine", "Parmin", "Matthew", "Kailin", "Annan")
        val friendsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, friendsList)

        binding.tagFriendsDropdown.setAdapter(friendsAdapter)
        binding.tagFriendsDropdown.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        // Store selected friends
        val selectedFriends = mutableSetOf<String>()

        // Handle tag selection
        binding.tagFriendsDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedFriend = friendsAdapter.getItem(position) ?: return@setOnItemClickListener

            // Prevent duplicate selection
            if (selectedFriends.add(selectedFriend)) {
                addChipToGroup(selectedFriend, binding.tagFriendsChipGroup, selectedFriends)
            }
            binding.tagFriendsDropdown.text.clear()
        }
    }

    private fun setupRestaurantTagging() {
        val restaurantList = listOf("Pizza Palace", "Sushi World", "Burger Haven", "Taco Town", "Pasta Paradise")
        val restaurantAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, restaurantList)

        binding.tagRestaurantsDropdown.setAdapter(restaurantAdapter)
        binding.tagRestaurantsDropdown.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        // Store selected restaurants
        val selectedRestaurants = mutableSetOf<String>()

        // Handle tag selection
        binding.tagRestaurantsDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedRestaurant = restaurantAdapter.getItem(position) ?: return@setOnItemClickListener

            // Prevent duplicate selection
            if (selectedRestaurants.add(selectedRestaurant)) {
                addChipToGroup(selectedRestaurant, binding.tagRestaurantChipGroup, selectedRestaurants)
                binding.tagRestaurantsDropdown.text.clear()
            }
            binding.tagRestaurantsDropdown.text.clear()
        }
    }


    private fun addChipToGroup(text: String, chipGroup: ChipGroup, selectedItems: MutableSet<String>) {
        val chip = Chip(this).apply {
            this.text = text
            this.isCloseIconVisible = true
            this.setOnCloseIconClickListener {
                chipGroup.removeView(this)
                selectedItems.remove(text)
            }
        }
        chipGroup.addView(chip)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUserTagging()
        setupRestaurantTagging()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Leave a Review"

        binding.submitReviewButton.setOnClickListener {
            val overallRating = binding.overallRatingBar.rating.toDouble()
            val reviewText = binding.reviewText.text.toString().trim()

            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newReview = Review(
                reviewID = (0..1000).random(),
                poster = "Anonymous",
                caption = reviewText,
                photos = selectedPhotos,
                taggedUsers = taggedUsers,
                restaurants = taggedRestaurants,
                location = selectedLocation,
                date = Instant.now(),
                rating = overallRating,
                likes = 0
            )

            reviewViewModel.addReview(newReview)
            Toast.makeText(this, "Review Submitted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
