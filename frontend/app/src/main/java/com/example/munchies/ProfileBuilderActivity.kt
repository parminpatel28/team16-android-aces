package com.example.munchies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.munchies.api.CreateUserRequest
import com.example.munchies.api.UserService
import com.example.munchies.databinding.ActivityProfileBuilderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ProfileBuilderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBuilderBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null
    private lateinit var userService: UserService
    
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.profilePictureImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBuilderBinding.inflate(layoutInflater)
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

        binding.profilePictureImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.finishButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            val email = binding.emailEditText.text.toString()

            if (validateInputs(name, username, email)) {
                uploadProfileData(name, username, bio, email)
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

    private fun uploadProfileData(name: String, username: String, bio: String, email: String) {
        val userId = auth.currentUser?.uid ?: return
        
        // If there's a profile picture, upload it first
        if (selectedImageUri != null) {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("profile_pictures/$userId.jpg")
            
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        createUserProfile(userId, name, username, bio, email, downloadUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Save profile without image
            createUserProfile(userId, name, username, bio, email, null)
        }
    }

    private fun createUserProfile(
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
                val response = userService.createUser(request)
                if (response.isSuccessful) {
                    startActivity(Intent(this@ProfileBuilderActivity, MainActivity::class.java))
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("ProfileBuilder", "Error creating profile: $errorBody")
                    android.util.Log.e("ProfileBuilder", "Response code: ${response.code()}")
                    
                    // Show a dialog with the full error message
                    androidx.appcompat.app.AlertDialog.Builder(this@ProfileBuilderActivity)
                        .setTitle("Error Creating Profile")
                        .setMessage("Failed to create profile:\n${errorBody ?: "Unknown error"}")
                        .setPositiveButton("OK", null)
                        .show()
                }
            } catch (e: Exception) {
                android.util.Log.e("ProfileBuilder", "Exception creating profile", e)
                
                // Show a dialog with the full error message
                androidx.appcompat.app.AlertDialog.Builder(this@ProfileBuilderActivity)
                    .setTitle("Error Creating Profile")
                    .setMessage("Error creating profile:\n${e.message}\n\n${e.stackTraceToString()}")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
} 