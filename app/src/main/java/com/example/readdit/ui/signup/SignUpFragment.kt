package com.example.readdit.ui.signup

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {

    // View binding
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    // Progress bar
    private lateinit var progressBar: ProgressBar

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Firebase Firestore
    private val db = Firebase.firestore

    // User data
    private var username = ""
    private var email = ""
    private var password = ""
    private var userID = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize progress bar
        progressBar = binding.progressBar

        // Redirect user to sign in page
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_sign_up_to_navigation_sign_in)
        }

        // When user click the "Sign Up" button
        binding.signUpButton.setOnClickListener {
            // Validate data before logging in
            validateData()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateData() {
        // Get the user data
        email = binding.signUpEmailEt.text.toString().trim()
        password = binding.signUpPwEt.text.toString().trim()
        username = binding.signUpUsernameEt.text.toString().trim()

        // Validate data
        if(TextUtils.isEmpty(username)) {
            // No password entered
            binding.signUpUsernameEt.error = "Username cannot be blank"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Invalid email format
            binding.signUpEmailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)) {
            // No password entered
            binding.signUpPwEt.error = "Please enter password"
        }
        else if(password.length < 8) {
            binding.signUpPwEt.error = "Password must be at least 8 characters"
        }
        else {
            // Data are all validated, begin logging in
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // Show progress
        progressBar.visibility = View.VISIBLE

        // Create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign up success
                progressBar.visibility = View.INVISIBLE

                // Get user info
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    userID = firebaseUser.uid
                }

                // Store the user info
                val user = hashMapOf(
                    "email" to email,
                    "password" to password,
                    "username" to username,
                    "isReminderOn" to false,
                    "reminderHour" to null,
                    "reminderMin" to null
                )

                // Add the user to firebase
                db.collection("user").document(userID)
                    .set(user)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                // Display toast message
                Toast.makeText(requireContext(), "Successfully created account", Toast.LENGTH_SHORT).show()

                // Redirect user to set up profile screen
                findNavController().navigate(R.id.action_navigation_sign_up_to_navigation_home)
            }
            .addOnFailureListener { e->
                // Sign up failed
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Sign up failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}