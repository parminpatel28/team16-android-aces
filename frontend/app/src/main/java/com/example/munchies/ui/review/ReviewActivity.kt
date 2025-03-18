package com.example.munchies.ui.review

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityReviewBinding
import com.example.munchies.model.Review
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.time.Instant


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

        // Handle tag selection
        binding.tagRestaurantsDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedRestaurant = restaurantAdapter.getItem(position) ?: return@setOnItemClickListener

            // Prevent duplicate selection
            if (!taggedRestaurants.contains(selectedRestaurant)) {
                taggedRestaurants.add(selectedRestaurant)
                addChipToGroupList(selectedRestaurant, binding.tagRestaurantChipGroup)
            }
            binding.tagRestaurantsDropdown.text.clear()
        }
    }

    private fun addChipToGroupList(text: String, chipGroup: ChipGroup) {
        val chip = Chip(this).apply {
            this.text = text
            this.isCloseIconVisible = true
            this.setOnCloseIconClickListener {
                chipGroup.removeView(this)
                taggedRestaurants.remove(text)
            }
        }
        chipGroup.addView(chip)
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

        // Get pre-filled restaurant info if available
        val restaurantName = intent.getStringExtra("RESTAURANT_NAME")
        val restaurantId = intent.getStringExtra("RESTAURANT_ID")
        val restaurantAddress = intent.getStringExtra("RESTAURANT_ADDRESS")

        // Pre-fill restaurant if provided
        if (restaurantName != null) {
            binding.tagRestaurantsDropdown.setText(restaurantName)
            // Add the restaurant as a chip
            if (!taggedRestaurants.contains(restaurantName)) {
                taggedRestaurants.add(restaurantName)
                addChipToGroupList(restaurantName, binding.tagRestaurantChipGroup)
            }
        }

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
                location = selectedLocation ?: "",
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
