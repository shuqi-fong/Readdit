package com.example.readdit.ui.notes

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readdit.R
import com.example.readdit.databinding.FragmentNotesBinding
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.explore.ExploreAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*

class NotesFragment : Fragment() ,NotesAdapter.OnItemClickListener
{

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var notesList: ArrayList<NotesData>
    private lateinit var notesAdapter: NotesAdapter
    private var _binding: FragmentNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addNotes.setOnClickListener() {
            findNavController().navigate(R.id.action_navigation_thoughts_to_navigation_add_notes)
        }
        notesList = arrayListOf()
        notesAdapter = NotesAdapter(requireContext(),notesList,this)
        binding.recyclerView.adapter = notesAdapter
        listNotes()
        return root
    }

    private fun listNotes() {
        db = FirebaseFirestore.getInstance()
        db.collection("user").document("user001").collection("notes")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firestore Error", error.message.toString())
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            notesList.add(dc.document.toObject(NotesData::class.java))
                        }
                    }
                    Log.d("kfc",notesList.toString())
                    notesAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

}