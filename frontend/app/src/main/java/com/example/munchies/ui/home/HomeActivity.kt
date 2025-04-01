package com.example.munchies.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home) // Activity context
        val placeID = intent.getStringExtra("placeID")
        val fromMap = intent.getBooleanExtra("fromMap", false)

        val fragment = HomeFragment().newInstance(placeID, fromMap)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home, fragment)
                .commit()
        }
    }
}