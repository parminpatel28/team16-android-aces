package com.example.munchies.model

import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: String,
    val name: String,
    val address: String,
    val rating: Double?,
    val location: LatLng
) 