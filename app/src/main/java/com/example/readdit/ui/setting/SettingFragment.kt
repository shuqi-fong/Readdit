package com.example.readdit.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {
    // View Model Binding
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize bindings
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Redirect user to account page
        binding.accountButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_account)
        }

        // Redirect user to notification page
        binding.notificationButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_notification)
        }

        // Redirect user to about page
        binding.aboutButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_about)
        }

        // Log the user out and redirect user to splash screen
        binding.logOutButton.setOnClickListener{
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Successfully logged out", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_setting_to_navigation_splash_screen)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}