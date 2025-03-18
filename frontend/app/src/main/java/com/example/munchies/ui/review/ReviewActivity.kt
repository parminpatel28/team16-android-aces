package com.example.munchies.ui.review

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.MainActivity
import com.example.munchies.api.ApiClient
import com.example.munchies.api.UserApiService
import com.example.munchies.api.UserService
import com.example.munchies.databinding.ActivityReviewBinding
import com.example.munchies.model.Location
import com.example.munchies.model.Review
import com.example.munchies.model.User
import com.example.munchies.model.UserManager
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.ReviewRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.time.Instant
import com.google.firebase.auth.FirebaseAuth


class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private lateinit var userService: UserService
    private var taggedRestaurants: MutableList<String> = mutableListOf()
    private var selectedLocation: String? = null
    private val repository = FriendRepository()
    private val userId = "UPcDzQ2iSuZZkTYDAKtuatiSe7m2" // FirebaseAuth.getInstance().currentUser?.uid

    private fun loadUserIfNeeded(userId: String) {
        if (UserManager.currentUser == null) {
            repository.fetchUserById(userId) { user ->
                if (user != null) {
                    println("User fetched: ${user.username}")
                } else {
                    println("Failed to fetch user")
                }
            }
        } else {
            println("User already loaded: ${UserManager.currentUser?.username}")
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

        Log.d("ReviewActivity", "onCreate: ReviewActivity started")

        if (userId != null) {
            loadUserIfNeeded(userId)
        }
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

        setupRestaurantTagging()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Leave a Review"

        binding.submitReviewButton.setOnClickListener {
            Log.d("ReviewActivity", "Submit Review clicked")
            try{
                val overallRating = binding.overallRatingBar.rating.toDouble()
                val reviewText = binding.reviewText.text.toString().trim()

                if (reviewText.isEmpty()) {
                    Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (overallRating < 0.5) {
                    Toast.makeText(this, "Rating must be >= 0.5 stars", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val review = UserManager.currentUser?.let { it1 ->
                    Review(
                        reviewID = 0,
                        user = it1,
                        caption = reviewText,
                        photos = emptyList(),
                        location = Location(id = 1), // selectedLocation ?: "",
                        date = Instant.now().toString(),
                        rating = overallRating,
                        likes = 0
                    )
                }

                Log.d("ReviewActivity", "Submitting Review")
                if (review != null) {
                    reviewViewModel.submitReview(review)
                }

                Toast.makeText(this, "Review Submitted!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Log.e("ReviewActivity", "Crash in submit button: ${e.message}")
                e.printStackTrace()
            }
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
