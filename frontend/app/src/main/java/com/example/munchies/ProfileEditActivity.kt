package com.example.munchies

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.munchies.api.User
import com.example.munchies.api.UserService
import com.example.munchies.databinding.ActivityProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        binding.usernameEditText.setText(user?.getString("userName"))
        binding.bioEditText.setText(user?.getString("userBio"))
        binding.emailEditText.setText(user?.getString("userEmail"))

        Glide.with(this).load(user?.getString("userPfp")).into(binding.profilePictureImageView)

        binding.profilePictureImageView.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }


    }





}