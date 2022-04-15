package com.example.readdit.ui.signin

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    // View Model Binding
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    // Progress bar
    private lateinit var progressBar: ProgressBar

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // User data
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize progress bar
        progressBar = binding.progressBar

        // Redirect user to sign up page
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_sign_in_to_navigation_sign_up)
        }

        // Validate user input before logging in
        binding.signInButton.setOnClickListener {
            validateData()
        }

        // Redirect user to forgot password page
        binding.forgotPw.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_sign_in_to_navigation_forgot_password)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateData() {
        // Get the user data
        email = binding.signInEmailEt.text.toString().trim()
        password = binding.signInPwEt.text.toString().trim()

        // Validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Invalid email format
            binding.signInEmailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)) {
            // No password entered
            binding.signInPwEt.error = "Please enter password"
        }
        else {
            // Data are all validated, begin logging in
            firebaseSignIn()
        }
    }

    private fun firebaseSignIn() {
        // Show progress
        progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Sign in success
                progressBar.visibility = View.INVISIBLE

                // Show toast message
                Toast.makeText(requireContext(), "Successfully signed in", Toast.LENGTH_SHORT).show()

                // Redirect user to home screen
                findNavController().navigate(R.id.action_navigation_sign_in_to_navigation_home)
            }
            .addOnFailureListener { e->
                // Sign In failed
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}