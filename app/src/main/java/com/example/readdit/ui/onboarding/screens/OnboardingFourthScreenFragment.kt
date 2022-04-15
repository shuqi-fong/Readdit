package com.example.readdit.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentOnboardingFourthScreenBinding

class OnboardingFourthScreenFragment : Fragment() {
    // View binding
    private var _binding: FragmentOnboardingFourthScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentOnboardingFourthScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configure view pager
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_onboarding_view_pager_to_navigation_sign_up)
        }

        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_onboarding_view_pager_to_navigation_sign_in)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}