package com.example.readdit.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.readdit.R
import com.example.readdit.databinding.FragmentOnboardingFirstScreenBinding

class OnboardingFirstScreenFragment : Fragment() {
    // View binding
    private var _binding: FragmentOnboardingFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentOnboardingFirstScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configure view pager
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        binding.nextButton.setOnClickListener{
            viewPager?.currentItem = 1
        }

        binding.skipButton.setOnClickListener{
            viewPager?.currentItem = 3
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}