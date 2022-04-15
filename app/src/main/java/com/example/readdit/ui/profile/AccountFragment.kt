package com.example.readdit.ui.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    // View binding
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    // User data
    private lateinit var userID : String
    private lateinit var usernameTv : TextView
    private lateinit var emailTv : TextView
    private lateinit var passwordTv : TextView

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    // Firebase Firestore
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get data field's text view
        usernameTv = binding.username
        emailTv = binding.email
        passwordTv = binding.password

        // Initialize Firebase Authentication, Firebase Cloud Storage
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get user id & reference
        userID = firebaseAuth.currentUser!!.uid
        val documentReference : DocumentReference = db.collection("user").document(userID)

        // Retrieve data from database
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }

                // Display data from db
                usernameTv.text = document.getString("username")
                emailTv.text = document.getString("email")
                passwordTv.text = document.getString("password")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        // Redirect user to edit account page
        usernameTv.setOnClickListener {
            val action = AccountFragmentDirections.actionNavigationAccountToNavigationEditAccountData(getString(R.string.username_df))

            findNavController().navigate(action)
        }

        emailTv.setOnClickListener {
            val action = AccountFragmentDirections.actionNavigationAccountToNavigationEditAccountData(getString(R.string.email_df))

            findNavController().navigate(action)
        }

        passwordTv.setOnClickListener {
            val action = AccountFragmentDirections.actionNavigationAccountToNavigationEditAccountData(getString(R.string.password_df))

            findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}