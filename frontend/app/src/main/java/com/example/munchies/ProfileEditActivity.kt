package com.example.munchies

import android.content.Intent
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
import java.util.*

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
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

        Glide.with(this).load("https://e.snmc.io/i/fullres/w/92a83a11be8d457d5fc32ac7477db0c3/11130567").into(binding.profilePictureImageView)

        binding.profilePictureImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }


    }





}