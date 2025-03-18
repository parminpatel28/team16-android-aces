package com.example.munchies.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.munchies.LoginActivity
import com.example.munchies.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
<<<<<<< HEAD
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.example.munchies.ProfileEditActivity
import com.example.munchies.ui.review.ReviewActivity
import com.google.android.material.textfield.TextInputEditText
import java.net.URL
=======
>>>>>>> 322dd40efee7449da3ec05db121ba53709914d6f

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

<<<<<<< HEAD
        // Bind UI elements
        val textName: TextView = binding.textProfileName
        val textEmail: TextView = binding.textProfileEmail
        val textReviews: TextView = binding.textProfileReviews
        val textFriends: TextView = binding.textProfileFriends
        val imagePfp: ImageView = binding.imageProfilePicture
        val imageEdit: ImageView = binding.imageEdit
        val btnLogout: Button = binding.btnLogout
        val textBio: TextView = binding.textProfileBio
        val textInputBio: TextInputEditText = binding.textInputProfileBio
=======
        setupObservers()
        setupClickListeners()
>>>>>>> 322dd40efee7449da3ec05db121ba53709914d6f

        return binding.root
    }

    private fun setupObservers() {
        profileViewModel.userName.observe(viewLifecycleOwner) {
            binding.textProfileName.text = it
        }

        profileViewModel.userEmail.observe(viewLifecycleOwner) {
            binding.textProfileEmail.text = it
        }

<<<<<<< HEAD
        profileViewModel.userPfp.observe(viewLifecycleOwner) {
            Glide.with(context).load(it).into(imagePfp)
=======
        profileViewModel.userBio.observe(viewLifecycleOwner) {
            binding.textProfileBio.text = it
            binding.textInputProfileBio.hint = it
>>>>>>> 322dd40efee7449da3ec05db121ba53709914d6f
        }

        profileViewModel.userPfp.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(url)
                    .into(binding.imageProfilePicture)
            }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.profileContent.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        profileViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm logout?")
                .setPositiveButton("Confirm") { _, _ ->
                    profileViewModel.logout()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

<<<<<<< HEAD
        imageEdit.setOnClickListener{
            startActivity(Intent(requireContext(), ProfileEditActivity::class.java))
        }

        return root
=======
        binding.swipeRefreshLayout.setOnRefreshListener {
            profileViewModel.refreshUserData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
>>>>>>> 322dd40efee7449da3ec05db121ba53709914d6f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
