package com.example.munchies.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R

class PlaceReviewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_reviews)
        supportActionBar?.hide()

        val placeID = intent.getStringExtra("placeID")
        val fragment = PlaceReviewsFragment.newInstance(placeID!!)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_reviews_container, fragment)
                .commit()
        }
    }
}
