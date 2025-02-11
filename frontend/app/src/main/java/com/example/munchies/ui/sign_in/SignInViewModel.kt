package com.example.munchies.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInResult = MutableLiveData<SignInResult>()
    val signInResult: LiveData<SignInResult> get() = _signInResult

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signInResult.value = SignInResult(false, "Please enter both email and password.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in successful
                    _signInResult.value = SignInResult(true, "")
                } else {
                    // Sign-in failed
                    _signInResult.value = SignInResult(false, task.exception?.message ?: "Sign-in failed.")
                }
            }
    }
}

data class SignInResult(val success: Boolean, val errorMessage: String?)
