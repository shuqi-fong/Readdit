package com.example.readdit.ui.profile

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.readdit.R
import com.example.readdit.databinding.FragmentEditAccountDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditAccountDataFragment : Fragment() {
    // View binding
    private var _binding: FragmentEditAccountDataBinding? = null
    private val binding get() = _binding!!

    // Get arguments passed from another fragment
    private val args: EditAccountDataFragmentArgs by navArgs()

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Firebase Firestore
    private lateinit var db : FirebaseFirestore
    private lateinit var documentReference : DocumentReference

    // User data
    private var userID = ""
    private var input = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentEditAccountDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication, Firebase Cloud Storage
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get user id & reference
        userID = firebaseAuth.currentUser!!.uid
        documentReference = db.collection("user").document(userID)

        // Update header text view
        binding.editProfileDataHeader.text = "Edit " + args.dataField

        // Handle when user click into different data field & "Save" button
        if(args.dataField == getString(R.string.username_df)) {
            binding.editUsernameTil.visibility = View.VISIBLE

            binding.saveButton.setOnClickListener {
                input = binding.editUsernameEt.text.toString().trim()
                validateUsername()
            }
        } else if (args.dataField == getString(R.string.email_df)) {
            binding.editEmailTil.visibility = View.VISIBLE

            binding.saveButton.setOnClickListener {
                input = binding.editEmailEt.text.toString().trim()
                validateEmail()
            }
        } else if (args.dataField == getString(R.string.password_df)) {
            binding.editPwTil.visibility = View.VISIBLE

            binding.saveButton.setOnClickListener {
                input = binding.editPwEt.text.toString().trim()
                validatePassword()
            }
        }

        // Redirect user to edit account button
        binding.discardButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_edit_account_data_to_navigation_account)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateUsername() {
        if(TextUtils.isEmpty(input)) {
            // No username entered
            binding.editUsernameEt.error = "Username cannot be blank"
        } else {
            updateUsername()
        }
    }

    private fun validateEmail() {
        if(!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            // Invalid email format
            binding.editEmailEt.error = "Invalid email format"
        } else {
            updateEmail()
        }
    }

    private fun validatePassword() {
        if(TextUtils.isEmpty(input)) {
            // No password entered
            binding.editPwEt.error = "Please enter password"
        } else if(input.length < 8) {
            binding.editPwEt.error = "Password must be at least 8 characters"
        } else {
            updatePassword()
        }
    }

    private fun updateUsername() {
        documentReference.update("username", input)
            .addOnSuccessListener {
                Log.d(tag, "Username successfully changed:$input")

                // Redirect user to account page
                findNavController().navigate(R.id.action_navigation_edit_account_data_to_navigation_account)
            }
            .addOnFailureListener {
                e -> Log.w(tag, "Error occurred: ", e)
            }
    }

    private fun updateEmail() {
        documentReference.update("email", input)
            .addOnSuccessListener {
                Log.d(tag, "Email successfully changed:$input")
                // Redirect user to account page
                findNavController().navigate(R.id.action_navigation_edit_account_data_to_navigation_account)
            }
            .addOnFailureListener {
                e -> Log.w(tag, "Error occurred: ", e)
            }
    }

    private fun updatePassword() {
        documentReference.update("password", input)
            .addOnSuccessListener {
                Log.d(tag, "Password successfully changed:$input")
                // Redirect user to account page
                findNavController().navigate(R.id.action_navigation_edit_account_data_to_navigation_account)
            }
            .addOnFailureListener {
                e -> Log.w(tag, "Error occurred: ", e)
            }
    }
}