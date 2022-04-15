package com.example.readdit.ui.signin

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {
    // View Model Binding
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // When user click "Send" button
        binding.sendButton.setOnClickListener {
            validateData()
        }

        // When user click "Open Mail" button
        binding.openMailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)

            try {
                startActivity(intent)
            }
            catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No supporting software available to open.", Toast.LENGTH_SHORT).show()
            }
        }

        // When user click "Back to Sign In" button
        binding.backToSignInButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_forgot_password_to_navigation_sign_in)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateData() {
        // Get the user data
        val email = binding.forgotPwEmailEt.text.toString().trim()

        // Validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Invalid email format
            binding.forgotPwEmailEt.error = "Invalid email format"
        }
        else {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(context, "Link has been sent. Please check your mailbox.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Unknown error occurred.", Toast.LENGTH_SHORT).show()
                }
        }
    }

}