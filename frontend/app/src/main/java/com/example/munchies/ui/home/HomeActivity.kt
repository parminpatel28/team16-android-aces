package com.example.munchies.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.munchies.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home) // Activity context

        val fragment = HomeFragment()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home, fragment)
                .commit()
        }
    }

    companion object {
        fun newInstance(placeID: String?, fromMap: Boolean): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle().apply {
                putString("placeID", placeID)
                putBoolean("fromMap", fromMap)
            }
            fragment.arguments = args
            return fragment
        }
    }
}