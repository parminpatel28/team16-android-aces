package com.example.munchies.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchies.R
import com.example.munchies.databinding.FragmentMapBinding
import com.example.munchies.model.Place
import com.example.munchies.ui.home.HomeActivity
import com.example.munchies.ui.review.ReviewActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place as GooglePlace
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var placeAdapter: PlaceAdapter
    private var searchJob: Job? = null
    private val searchScope = CoroutineScope(Dispatchers.Main)

    // Default location (Waterloo, Ontario)
    private val defaultLocation = LatLng(43.4643, -80.5204)
    // Search radius in degrees (approximately 10km)
    private val searchRadius = 0.1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calledByAddReview = arguments?.getBoolean("fromReview")

        // parmin
        /* review Persistence */
        val rating = arguments?.getFloat("rating", 0.0f)
        val caption = arguments?.getString("caption")
        /* review Persistence */

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyDk5Au-odk_HsBK8dRT_6GvFWnwS8EeQjA")
        }
        placesClient = Places.createClient(requireContext())

        // Initialize RecyclerView
        placeAdapter = PlaceAdapter(
            onPlaceClick = { place ->
                // Handle place selection
                googleMap.clear()
                googleMap.addMarker(
                    MarkerOptions()
                        .position(place.location)
                        .title(place.name)
                        .snippet(place.address)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.location, 15f))
                binding.searchResultsRecyclerView.visibility = View.GONE
            },
            onWriteReviewClick = { place ->
                // Launch ReviewActivity with restaurant info
                val intent = Intent(requireContext(), ReviewActivity::class.java).apply {
                    putExtra("RESTAURANT_NAME", place.name)
                    putExtra("RESTAURANT_ID", place.id)
                    putExtra("RESTAURANT_ADDRESS", place.address)

                    putExtra("rating", rating)
                    putExtra("caption", caption)

                }
                startActivity(intent)
                activity?.finish()
            },
            onViewReviewsClick = { place ->
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.putExtra("placeID", place.id)
                intent.putExtra("fromMap", true)
                Log.d("OnViewReviewsClick", place.address)
                startActivity(intent)
            }
        )

        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = placeAdapter
        }

        if (calledByAddReview == true) {
            binding.returnButton.isEnabled = true
            binding.returnButton.visibility = VISIBLE
        } else {
            binding.returnButton.isEnabled = false
            binding.returnButton.visibility = INVISIBLE
        }
        binding.returnButton.setOnClickListener {
            binding.returnButton.isEnabled = false
            binding.returnButton.visibility = INVISIBLE
            activity?.finish()
        }

        // Set up search functionality
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchPlaces(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchPlaces(it) }
                return true
            }
        })

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
    }

    private fun searchPlaces(query: String) {
        searchJob?.cancel()
        searchJob = searchScope.launch {
            try {
                // Create a rectangular bounds around the default location
                val bounds = RectangularBounds.newInstance(
                    LatLng(defaultLocation.latitude - searchRadius, defaultLocation.longitude - searchRadius),
                    LatLng(defaultLocation.latitude + searchRadius, defaultLocation.longitude + searchRadius)
                )

                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(query)
                    .setLocationBias(bounds)
                    .setTypesFilter(listOf("restaurant"))
                    .build()

                val response = withContext(Dispatchers.IO) {
                    placesClient.findAutocompletePredictions(request).await()
                }

                val places = response.autocompletePredictions.map { prediction ->
                    // Fetch place details
                    val placeRequest = FetchPlaceRequest.builder(
                        prediction.placeId,
                        listOf(
                            GooglePlace.Field.ID,
                            GooglePlace.Field.NAME,
                            GooglePlace.Field.LAT_LNG,
                            GooglePlace.Field.ADDRESS,
                            GooglePlace.Field.RATING
                        )
                    ).build()

                    val placeResponse = withContext(Dispatchers.IO) {
                        placesClient.fetchPlace(placeRequest).await()
                    }

                    val place = placeResponse.place
                    Place(
                        id = place.id,
                        name = place.name,
                        address = place.address ?: "",
                        rating = place.rating,
                        location = place.latLng ?: LatLng(0.0, 0.0)
                    )
                }

                placeAdapter.submitList(places)
                binding.searchResultsRecyclerView.visibility = if (places.isEmpty()) View.GONE else View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // parmin
    fun newInstance(fromReview : Boolean, /* review Persistence */ rating : Float, caption : String? /* review Persistence */): MapFragment {
        val fragment = MapFragment()

        val bundle = Bundle().apply {
            putBoolean("fromReview", fromReview)
            /* review Persistence */
            putString("caption", caption)
            putFloat("rating", rating)
            /* review Persistence */
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Waterloo"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchJob?.cancel()
    }
}
