package com.example.munchies

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
            val username = binding.usernameEditText.text.toString()
            val location = binding.locationEditText.text.toString()
            val bio = binding.bioEditText.text.toString()
            val profilePicture = "https://example.com/pic.jpg" // Placeholder profile picture URL
            val email = auth.currentUser?.email // Get the email of the currently signed-in user

            if (email != null && username.isNotEmpty()) {
                // Create the UserProfile object
                val userProfile = UserProfile(
                    name = "John Doe", // This could be collected as well from the form
                    username = username,
                    profilePicture = profilePicture,
                    userBio = bio,
                    emailAddress = email,
                )

                // Call ViewModel to handle the network request
                viewModel.createUserProfile(userProfile)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the success status from the ViewModel
        viewModel.profileUpdateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Navigate to MainActivity if successful
                Toast.makeText(requireContext(), "Profile Created!", Toast.LENGTH_SHORT).show()
                // You can use navigation here, e.g., findNavController().navigate(R.id.action_profileBuilderFragment_to_mainActivity)
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
