package com.example.munchies.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.munchies.R
import com.example.munchies.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMap
        mapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val location = LatLng(43.4643, -80.5204) // Coordinates for Waterloo, Ontario
        googleMap.addMarker(MarkerOptions().position(location).title("Marker in Waterloo"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
