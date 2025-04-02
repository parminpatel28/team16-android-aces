package com.example.munchies.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.munchies.R

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val choosingLocation = intent.getBooleanExtra("fromReview", true)

        val caption = intent.getStringExtra("caption")
        val rating = intent.getFloatExtra("rating", 0.0f)

        val fragment = MapFragment().newInstance(choosingLocation, rating, caption)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.map, fragment)
                .commit()
        }
    }
}