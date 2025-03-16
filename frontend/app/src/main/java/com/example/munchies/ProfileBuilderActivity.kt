package com.example.munchies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityProfileBuilderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import java.util.*

class ProfileBuilderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBuilderBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null
    
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
                        saveUserProfile(userId, name, username, bio, email, downloadUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Save profile without image
            saveUserProfile(userId, name, username, bio, email, null)
        }
    }

    private fun saveUserProfile(
        userId: String,
        name: String,
        username: String,
        bio: String,
        email: String,
        profilePictureUrl: String?
    ) {
        // TODO: Implement your backend API call here to save the user profile
        // For now, we'll just redirect to MainActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
} 