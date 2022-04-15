package com.example.readdit.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.readdit.databinding.FragmentOnboardingViewPagerBinding
import com.example.readdit.ui.onboarding.screens.OnboardingFirstScreenFragment
import com.example.readdit.ui.onboarding.screens.OnboardingFourthScreenFragment
import com.example.readdit.ui.onboarding.screens.OnboardingSecondScreenFragment
import com.example.readdit.ui.onboarding.screens.OnboardingThirdScreenFragment

class OnboardingViewPagerFragment : Fragment() {

    // View binding
    private var _binding: FragmentOnboardingViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentOnboardingViewPagerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Fragment list that includes all screens to be display
        val fragmentList = arrayListOf<Fragment>(
            OnboardingFirstScreenFragment(),
            OnboardingSecondScreenFragment(),
            OnboardingThirdScreenFragment(),
            OnboardingFourthScreenFragment()
        )

        val adapter = OnboardingViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}