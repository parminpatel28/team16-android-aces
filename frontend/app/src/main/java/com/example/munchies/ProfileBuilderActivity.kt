package com.example.munchies

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileBuilderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.saveProfileBtn.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            val location = binding.locationEditText.text.toString()

            if (name.isNotEmpty() && username.isNotEmpty()) {
                saveProfileData(name, username, bio, location)
            } else {
                Toast.makeText(this, "Name and Username are required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveProfileData(name: String, username: String, bio: String?, location: String?) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val profileData = mapOf(
                "name" to name,
                "username" to username,
                "bio" to bio,
                "location" to location
            )

            // Save to Firebase Realtime Database or Firestore (based on your setup)
            val database = FirebaseDatabase.getInstance().getReference("users/$userId")
            database.setValue(profileData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                    // Navigate to main screen or wherever you need
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save profile: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
