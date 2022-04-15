package com.example.readdit.ui.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        // Delay from navigating to next screen
        Handler().postDelayed({
            if(firebaseUser != null) {
                // If the user already logged in, redirect user to home screen
                findNavController().navigate(R.id.action_navigation_splash_screen_to_navigation_home)
            }
            else {
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_navigation_splash_screen_to_navigation_onboarding_view_pager)
                }
            }

        }, 3000)

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}