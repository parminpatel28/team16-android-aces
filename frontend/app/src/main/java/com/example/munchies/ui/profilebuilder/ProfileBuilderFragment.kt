package com.example.munchies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.munchies.databinding.FragmentProfileBuilderBinding
import com.example.munchies.model.UserProfile
import com.google.firebase.auth.FirebaseAuth

class ProfileBuilderFragment : Fragment() {

    private lateinit var viewModel: ProfileBuilderViewModel
    private var _binding: FragmentProfileBuilderBinding? = null
    private val binding get() = _binding!!

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBuilderBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileBuilderViewModel::class.java)

        // Set the button's click listener
        binding.saveProfileBtn.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val location = binding.locationEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            val profilePicture = "https://example.com/pic.jpg" // Placeholder profile picture URL
            val email = auth.currentUser?.email

            if (email != null && username.isNotEmpty()) {
                // Create the UserProfile object
                val userProfile = UserProfile(
                    name = name,
                    username = username,
                    profilePicture = profilePicture,
                    userBio = bio,
                    location_id = location,
                    emailAddress = email,
                    friends = emptyMap(),
                    savedReviews = emptyMap()
                )
                // Call ViewModel to handle the network request
                viewModel.createUserProfile(userProfile)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.profileUpdateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile Created!", Toast.LENGTH_SHORT).show()
                // Navigate to MainActivity
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
