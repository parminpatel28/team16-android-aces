package com.example.munchies.ui.review

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.example.munchies.R
import com.example.munchies.databinding.ActivityReviewDetailsBinding
import com.example.munchies.model.Review
import com.example.munchies.repository.ReviewRepository
import com.example.munchies.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.example.munchies.api.UserService
import com.example.munchies.model.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReviewDetailsActivity : AppCompatActivity() {

    private val repository = ReviewRepository()
    private val userRepository = UserRepository()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private lateinit var binding: ActivityReviewDetailsBinding

    private fun formatDate(instant: String): String {
        return try {
            val parsedInstant = Instant.parse(instant)
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())
            formatter.format(parsedInstant)
        } catch (e: Exception) {
            instant
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val review: Review? = intent.getParcelableExtra<Review>("review")
        review?.let {


            var likecount = it.likes
            binding.likesCount.text = likecount.toString()

            binding.heartIcon.setTag(false)
            if(review.liked != null){
                if(review.liked!!){
                    binding.heartIcon.setTag(review.liked)
                    binding.heartIcon.setImageResource(R.drawable.ic_heart_orange_fill)
                    likecount = likecount - 1
                }
            }

            binding.restaurantName.text = "Restaurant" // TODO: get location
            binding.reviewerName.text = it.user.name
            binding.reviewText.text = it.caption
            binding.overallRatingBar.rating = it.rating.toFloat()
            binding.reviewDate.text = formatDate(it.date)



            val container = binding.photosContainer
            container.removeAllViews() // Clear any existing images first

            val imageSize = resources.getDimensionPixelSize(R.dimen.review_photo_size) // e.g., 100dp

            binding.heartIcon.setOnClickListener{
                if (review.reviewID != null && userId != null){

                    userRepository.updateLikes(userId, review)

                    if (binding.heartIcon.getTag() == false){
                        repository.likeReview(review.reviewID)
                        binding.likesCount.text = (likecount + 1).toString()
                        binding.heartIcon.setImageResource(R.drawable.ic_heart_orange_24dp_fill)
                        binding.heartIcon.setTag(true)
                    }
                    else{
                        repository.dislikeReview(review.reviewID)
                        binding.likesCount.text = (likecount).toString()
                        binding.heartIcon.setImageResource(R.drawable.ic_heart_orange_24dp)
                        binding.heartIcon.setTag(false)
                    }

                }
            }

            it.photos?.forEach { photoUrl ->
                val imageView = ImageView(this)
                imageView.layoutParams = LinearLayout.LayoutParams(imageSize, imageSize).apply {
                    setMargins(8, 24, 8, 24)
                }
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get().load(photoUrl).into(imageView)

                imageView.setOnClickListener{
                    showFullscreenImage(photoUrl)
                }
                container.addView(imageView)
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showFullscreenImage(photoUrl: String) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        val imageView = ImageView(this)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setBackgroundColor(Color.BLACK)

        Picasso.get().load(photoUrl).into(imageView)

        imageView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(imageView)
        dialog.show()
    }



}
