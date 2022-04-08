package com.example.readdit.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.readdit.R
import com.example.readdit.databinding.FragmentArticleBinding
import com.example.readdit.databinding.FragmentHistoryBinding
import com.example.readdit.ui.article.ArticleAdapter
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.explore.ExploreAdapter
import com.google.firebase.firestore.*

class HistoryFragment : Fragment(), ArticleAdapter.OnItemClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var articleList: ArrayList<ArticleData>
    private lateinit var articleAdapter: ArticleAdapter
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        articleList = arrayListOf()
        articleAdapter = ArticleAdapter(requireContext(),articleList,this)
        binding.recyclerView.adapter = articleAdapter

        listFiles()
        return root
    }

    private fun listFiles() {
        db = FirebaseFirestore.getInstance()
        db.collection("article")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firestore Error", error.message.toString())
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            articleList.add(dc.document.toObject(ArticleData::class.java))
                        }
                    }
                    Log.d("kfc","$articleList")
                    articleAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(requireContext(),"Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}