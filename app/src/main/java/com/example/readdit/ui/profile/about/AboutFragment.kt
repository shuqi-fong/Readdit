package com.example.readdit.ui.profile.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.readdit.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    // View binding
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize binding
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // When user click the text, open the url in web browser
        binding.sourceCodeText.movementMethod = LinkMovementMethod.getInstance()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}