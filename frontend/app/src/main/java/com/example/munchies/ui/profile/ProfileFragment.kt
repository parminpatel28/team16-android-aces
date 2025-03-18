package com.example.munchies.ui.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.munchies.LoginActivity
import com.example.munchies.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.example.munchies.ProfileEditActivity
import com.example.munchies.ui.review.ReviewActivity
import com.google.android.material.textfield.TextInputEditText
import java.net.URL

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val context = requireActivity()

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        // Observe data from ViewModel and update UI
        profileViewModel.userName.observe(viewLifecycleOwner) {
            textName.text = it
        }
        profileViewModel.userEmail.observe(viewLifecycleOwner) {
            textEmail.text = it
        }
        profileViewModel.userReviews.observe(viewLifecycleOwner) {
            textReviews.text = "${it.size} reviews posted"
        }
        profileViewModel.userFriends.observe(viewLifecycleOwner) {
            textFriends.text = "${it.size} friends"
        }

        profileViewModel.userPfp.observe(viewLifecycleOwner) {
            Glide.with(context).load(it).into(imagePfp)
        }

        profileViewModel.userBio.observe(viewLifecycleOwner){
            textBio.text = it
            textInputBio.hint = it
        }

        // Handle Logout Button Click
        btnLogout.setOnClickListener {
            // Call the ViewModel to handle logout logic
            //profileViewModel.logout()
            val logoutBuilder = AlertDialog.Builder(requireContext())
            logoutBuilder.setTitle("Confirm logout?")
            logoutBuilder.setPositiveButton("Confirm") { dialog, which ->
                profileViewModel.logout()
                FirebaseAuth.getInstance().signOut()

                startActivity(Intent(context, LoginActivity::class.java))
            }
            logoutBuilder.setNegativeButton("Cancel") { dialog, which ->

            }
            logoutBuilder.create().show()
        }

        imageEdit.setOnClickListener{
            startActivity(Intent(requireContext(), ProfileEditActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
