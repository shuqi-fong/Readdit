package com.example.readdit.ui.explore

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.readdit.R
import com.example.readdit.databinding.FragmentExploreBinding
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment(),ExploreAdapter.OnItemClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var exploreList: ArrayList<ExploreData>
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var exploreViewModel: ExploreViewModel
    private var _binding: FragmentExploreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exploreViewModel =
            ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        exploreList = arrayListOf()
        exploreAdapter = ExploreAdapter(requireContext(),exploreList,this)
        binding.recyclerView.adapter = exploreAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        listFiles()
        return root
    }

    private fun listFiles() {
        db = FirebaseFirestore.getInstance()
        db.collection("explore")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("Firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        exploreList.add(dc.document.toObject(ExploreData::class.java))
                    }
                }
                exploreAdapter.notifyDataSetChanged()
            }
    }

    override fun onItemClick(position: Int) {
        val topicName = exploreList[position].topicName
        val action = ExploreFragmentDirections.actionNavigationExploreToNavigationArticle(topicName)
        findNavController().navigate(action)
        Toast.makeText(requireContext(),"Item $position clicked",Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}