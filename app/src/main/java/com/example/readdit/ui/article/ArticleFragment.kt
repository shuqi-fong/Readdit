package com.example.readdit.ui.article

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.readdit.databinding.FragmentArticleBinding
import com.example.readdit.ui.explore.ExploreFragmentDirections
import com.google.firebase.firestore.*

class ArticleFragment : Fragment(),ArticleAdapter.OnItemClickListener {

    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var articleList: ArrayList<ArticleData>
    private lateinit var articleAdapter: ArticleAdapter
    private var _binding: FragmentArticleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleViewModel =
            ViewModelProvider(this).get(ArticleViewModel::class.java)

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        articleList = arrayListOf()
        articleAdapter = ArticleAdapter(requireContext(),articleList,this)
        binding.recyclerView.adapter = articleAdapter

        listFiles()
        return root
    }

    private fun listFiles() {
        db = FirebaseFirestore.getInstance()
        db.collection("article").whereEqualTo("topic",args.topicName)
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
        Toast.makeText(requireContext(),"Item $position clicked",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}