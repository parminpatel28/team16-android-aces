package com.example.munchies.ui.sign_in

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R
import com.example.munchies.databinding.ActivitySignInScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SignInScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignInScreenBinding
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Call the ViewModel to handle the sign-in process
            signInViewModel.signIn(email, password)
        }

        // Observe ViewModel's authentication result
        signInViewModel.signInResult.observe(this, { result ->
            if (result.success) {
                // Navigate to home screen on success
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                // Navigate to the home screen
                // Example: startActivity(Intent(this, HomeActivity::class.java))
            } else {
                // Show error message if sign-in failed
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
