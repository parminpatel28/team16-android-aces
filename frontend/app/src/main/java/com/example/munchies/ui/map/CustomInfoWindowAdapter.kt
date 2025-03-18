package com.example.munchies.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.munchies.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null)

        val titleTextView = view.findViewById<TextView>(R.id.title)
        val snippetTextView = view.findViewById<TextView>(R.id.snippet)

        titleTextView.text = marker.title
        snippetTextView.text = marker.snippet

        return view
    }
} 