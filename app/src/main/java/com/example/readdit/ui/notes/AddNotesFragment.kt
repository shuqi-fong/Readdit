package com.example.readdit.ui.notes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentAddNotesBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddNotesFragment : Fragment() {

    private lateinit var discard: Button
    private lateinit var builder: AlertDialog.Builder
    private var _binding: FragmentAddNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        builder = AlertDialog.Builder(requireContext())
        val title = binding.noteEditTitle.text.toString()
        val body = binding.noteEditContent.text.toString()
        binding.discard.setOnClickListener() {
            builder.setTitle("Alert!")
                .setMessage("Do you want to discard this note?")
                .setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface,it ->
                    findNavController().navigate(R.id.action_navigation_add_notes_to_navigation_thoughts)
                }
                .setNegativeButton("No"){dialogInterface,it ->
                    dialogInterface.cancel()
                }
                .show()
        }
        binding.save.setOnClickListener() {
            builder.setTitle("Alert!")
                .setMessage("Do you want to save this note?")
                .setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface,it ->
                    addNotes(title, body)
                    findNavController().navigate(R.id.action_navigation_add_notes_to_navigation_thoughts)
                }
                .setNegativeButton("No"){dialogInterface,it ->
                    dialogInterface.cancel()
                }
                .show()
        }

        return root
    }

    private fun addNotes(title:String, body:String) {
        var db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "title" to title,
            "body" to body,
            "lastEdited" to FieldValue.serverTimestamp(),
        )
        db.collection("user").document("user001").collection("notes").add(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}