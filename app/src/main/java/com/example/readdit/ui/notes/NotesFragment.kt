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
import android.widget.SearchView
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
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.article.ArticleAdapter
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.explore.ExploreAdapter
import com.example.readdit.ui.home.HomeFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*

class NotesFragment : Fragment() ,NotesAdapter.OnItemClickListener
{

    private lateinit var ViewModel: ViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var notesList: ArrayList<NotesData>
    private lateinit var filteredNotesList: ArrayList<NotesData>
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
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        notesList = arrayListOf()
        ViewModel.notes.observe(viewLifecycleOwner, Observer {
            notesList = it
            notesAdapter = NotesAdapter(requireContext(),notesList,this)
            binding.recyclerView.adapter = notesAdapter
        })

        search()

        binding.addNotes.setOnClickListener() {
            findNavController().navigate(R.id.action_navigation_thoughts_to_navigation_add_notes)
        }

        return root
    }

    fun search(){
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(content: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(content: String?): Boolean {
                getNotesData()
                filter(content)
                return false
            }
        })
    }

    private fun getNotesData() {
        ViewModel.notes.observe(viewLifecycleOwner, Observer {
            notesList = it
            notesAdapter = NotesAdapter(requireContext(), notesList, this)
            binding.recyclerView.adapter = notesAdapter
        })
    }

    private fun filter(text: String?) {
        getNotesData()
        filteredNotesList = arrayListOf()
        notesAdapter = NotesAdapter(requireContext(), filteredNotesList, this)
        //looping through existing elements
        for (notes in notesList) {
            //if the existing elements contains the search input
            if (text != null) {
                if (notes.title.lowercase().contains(text.lowercase())) {
                    //adding the element to filtered list
                    filteredNotesList.add(notes)
                }
            }
        }
        //calling a method of the adapter class and passing the filtered list
        binding.recyclerView.adapter = notesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        notesAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(notes: NotesData) {
        val action = NotesFragmentDirections.actionNavigationThoughtsToNavigationSaveNotes(notes)
        findNavController().navigate(action)
    }

}