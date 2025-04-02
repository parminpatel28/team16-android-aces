package com.example.munchies

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.munchies.api.CreateUserRequest
import com.example.munchies.api.UserService
import com.example.munchies.databinding.ActivityProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null
    private lateinit var userService: UserService
    private var currentProfilePictureUrl: String? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.profilePictureImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var user = intent.extras

        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Android emulator localhost
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userService = retrofit.create(UserService::class.java)

        // Pre-fill email if available
        auth.currentUser?.email?.let { email ->
            binding.emailEditText.setText(email)
        }

        binding.nameEditText.setText(user?.getString("userName"))
        binding.usernameEditText.setText(user?.getString("userName")) // Using the same value for now
        binding.bioEditText.setText(user?.getString("userBio"))
        binding.emailEditText.setText(user?.getString("userEmail"))
        currentProfilePictureUrl = user?.getString("userPfp")

        Glide.with(this).load(currentProfilePictureUrl).into(binding.profilePictureImageView)

        binding.profilePictureImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.finishButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            val email = binding.emailEditText.text.toString()

            if (validateInputs(name, username, email)) {
                updateProfileData(name, username, bio, email)
            }
        }
    }

    private fun validateInputs(name: String, username: String, email: String): Boolean {
        if (name.isEmpty() || username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun updateProfileData(name: String, username: String, bio: String, email: String) {
        val userId = auth.currentUser?.uid ?: return
        
        // If there's a new profile picture, upload it first
        if (selectedImageUri != null) {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("profile_pictures/$userId.jpg")
            
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        updateUserProfile(userId, name, username, bio, email, downloadUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Update profile without new image, keep existing profile picture
            updateUserProfile(userId, name, username, bio, email, currentProfilePictureUrl)
        }
    }

    private fun updateUserProfile(
        userId: String,
        name: String,
        username: String,
        bio: String,
        email: String,
        profilePictureUrl: String?
    ) {
        val request = CreateUserRequest(
            firebaseUserId = userId,
            name = name,
            username = username,
            profilePicture = profilePictureUrl,
            userBio = bio.takeIf { it.isNotEmpty() },
            emailAddress = email
        )

        lifecycleScope.launch {
            try {
                val response = userService.updateUser(userId, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileEditActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Set result to trigger refresh in profile fragment
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@ProfileEditActivity, "Failed to update profile: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileEditActivity, "Error updating profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}