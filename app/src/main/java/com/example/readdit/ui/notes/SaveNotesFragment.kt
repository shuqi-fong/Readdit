package com.example.readdit.ui.notes

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.readdit.R
import com.example.readdit.databinding.FragmentAddNotesBinding
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.article.ArticleFragmentArgs
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SaveNotesFragment : Fragment() {

    private lateinit var discard: Button
    private lateinit var builder: AlertDialog.Builder
    private var _binding: FragmentAddNotesBinding? = null
    val args: SaveNotesFragmentArgs by navArgs()
    private lateinit var ViewModel: ViewModel
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

        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)
        val noteID = args.notesData.id
        builder = AlertDialog.Builder(requireContext())
        binding.noteEditTitle.setText(args.notesData.title)
        binding.noteEditContent.setText(args.notesData.body)
        binding.back.setOnClickListener(){
            findNavController().popBackStack()
        }
        binding.discard.setOnClickListener() {
            builder.setTitle("Alert!")
                .setMessage("Do you want to discard this note?")
                .setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface,it ->
                    binding.noteEditTitle.text = null
                    binding.noteEditContent.text = null
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
                    ViewModel.saveNotes(noteID,binding.noteEditTitle.toString(),binding.noteEditContent.toString())
                    Toast.makeText(requireContext(),"Your Note have been updated",Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No"){dialogInterface,it ->
                    dialogInterface.cancel()
                }
                .show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}