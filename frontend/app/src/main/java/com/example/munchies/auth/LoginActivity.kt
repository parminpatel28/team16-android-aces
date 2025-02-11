package com.example.munchies.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.ui.sign_in.SignInScreen
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // This should now work after creating the layout file

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if the user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // If the user is logged in, navigate to the main activity or home screen
            navigateToHomeScreen()
        } else {
            // If not, show the sign-in screen
            showSignInScreen()
        }
    }

    private fun navigateToHomeScreen() {
        // You can modify this to navigate to your main screen
        // For example:
        //startActivity(Intent(this, MainActivity::class.java))
        finish()  // Close LoginActivity once we are authenticated
    }

    private fun showSignInScreen() {
        // Start SignInScreen activity or Fragment
        startActivity(Intent(this, SignInScreen::class.java))
        finish()  // Close LoginActivity
    }
}
