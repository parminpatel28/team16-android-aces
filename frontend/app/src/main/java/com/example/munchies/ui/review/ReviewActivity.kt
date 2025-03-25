package com.example.munchies.ui.review

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.munchies.api.UserService
import com.example.munchies.databinding.ActivityReviewBinding
import com.example.munchies.model.Review
import com.example.munchies.model.UserManager
import com.example.munchies.repository.FriendRepository
import com.example.munchies.repository.ReviewRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.time.Instant
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okio.IOException
import java.io.File
import java.io.FileOutputStream


class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private lateinit var userService: UserService
    private var taggedRestaurants: MutableList<String> = mutableListOf()
    private var selectedLocation: String? = null
    private val repository = FriendRepository()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val reviewRepository = ReviewRepository();
    private var selectedImageUri: List<Uri> = emptyList();

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris != null) {

            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uris[0], flag)
            handleImageUrl(uris.take(3));
            selectedImageUri = uris.take(3);

            binding.tvFileName.visibility = View.INVISIBLE;
            Log.d("PhotoPicker", "Selected URI: $uris")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

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
        val restaurantAdapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, restaurantList)

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

        binding.btnChooseFile.setOnClickListener {
            pickMedia.launch("image/*")
        }

        binding.submitReviewButton.setOnClickListener {
            Log.d("ReviewActivity", "Submit Review clicked")
            try {
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

                val user = UserManager.currentUser
                if (user == null) {
                    Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val review = Review(
                    reviewID = null,  // Let backend assign this
                    user = user,
                    caption = reviewText,
                    photos = emptyList(),
                    date = Instant.now().toString(),
                    rating = overallRating,
                    likes = 0
                )

                reviewViewModel.submitReview(review) { reviewResponse ->
                    val reviewId = reviewResponse?.reviewID
                    val photos = mutableListOf<String>();
                    if (reviewId == null) {
                        Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
                        return@submitReview
                    }

                    Log.d("ReviewActivity", "Review submitted with ID: $reviewId")

                    // Upload images if any
                    if (selectedImageUri.isNotEmpty()) {
                        selectedImageUri.forEach { uri ->
                            val file = createTempFile(uri,)

                            if (file != null) {
                                val photoUrl = "https://munchies-ece452.s3.us-east-2.amazonaws.com/review/${reviewId}/${file.name}"
                                Log.d("ReviewActivity: ", photoUrl)
                                reviewRepository.requestPresignedUrl(
                                    file.name,
                                    reviewResponse.reviewID.toString(),
                                    onUrlReceived = { url ->
                                        uploadToS3(url, file)
                                    }
                                )
                                photos.add(photoUrl);
                            }
                        }
                    }
                    // update the review to have the photos
                    reviewRepository.updateReviewPhotos(reviewId, photos)

                    Toast.makeText(this, "Review Submitted!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Log.e("ReviewActivity", "Crash in submit button: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Error submitting review", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleImageUrl(uris: List<Uri>) {

        val ids = listOf(binding.selectedImageView.id, binding.selectedImageView2.id, binding.selectedImageView3.id);


        for (idx in uris.indices) {
            val imageView = findViewById<ImageView>(ids[idx])
            imageView.setImageURI(uris[idx])

        }
    }

    private fun createTempFile(uri: Uri): File? {

        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File(this.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input?.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun uploadToS3(presignedUrl: String, file: File) {
        Thread {
            val client = OkHttpClient()
            val mediaType = "image/*".toMediaTypeOrNull()  // Make sure this matches exactly
            val requestBody = file.asRequestBody(mediaType)

            val request = Request.Builder()
                .url(presignedUrl)
                .put(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("S3Upload", "Upload failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.d("S3Upload", "Upload successful: ${response.code}")
                    } else {
                        val errorBody = response.body?.string()
                        Log.e("S3Upload", "Upload failed with code: ${response.code}. Error: $errorBody")
                    }
                }
            })
        }.start()

    }
}
