package com.example.munchies.ui.friends
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R

class FriendDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_detail)

        val name = intent.getStringExtra("friend_name") ?: "Unknown"
        val username = intent.getStringExtra("friend_username") ?: "Unknown"

        val nameTextView: TextView = findViewById(R.id.textFriendDetailname)
        val usernameTextView: TextView = findViewById(R.id.textFriendDetailUserName)

        nameTextView.text = name
        usernameTextView.text = username
    }
}
