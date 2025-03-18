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

        setupObservers()
        setupClickListeners()

        return binding.root
    }

    private fun setupObservers() {
        profileViewModel.userName.observe(viewLifecycleOwner) {
            binding.textProfileName.text = it
        }

        profileViewModel.userEmail.observe(viewLifecycleOwner) {
            binding.textProfileEmail.text = it
        }

        profileViewModel.userBio.observe(viewLifecycleOwner) {
            binding.textProfileBio.text = it
            binding.textInputProfileBio.hint = it
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            profileViewModel.refreshUserData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
