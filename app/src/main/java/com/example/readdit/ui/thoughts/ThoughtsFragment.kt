package com.example.readdit.ui.thoughts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.readdit.R
import com.example.readdit.databinding.FragmentThoughtsBinding

class ThoughtsFragment : Fragment() {

    private lateinit var thoughtsViewModel: ThoughtsViewModel
    private var _binding: FragmentThoughtsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thoughtsViewModel = ViewModelProvider(this).get(ThoughtsViewModel::class.java)

        _binding = FragmentThoughtsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textThoughts
        thoughtsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
            Log.d("kfc",textView.text.toString())
        })
        Log.d("kfc",textView.text.toString())
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}