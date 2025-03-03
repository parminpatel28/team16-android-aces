package com.example.munchies

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        if (auth.currentUser != null) {
            // User is logged in, go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // User not logged in, go to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // Close this activity so it doesn't remain in the back stack
    }
}
