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
import com.example.readdit.MainActivity
import com.example.readdit.databinding.FragmentArticleBinding
import androidx.navigation.fragment.findNavController
import android.R
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.explore.ExploreFragmentDirections
import com.google.firebase.firestore.*

class ArticleFragment : Fragment(),ArticleAdapter.OnItemClickListener {

    private lateinit var ViewModel: ViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var result: ArrayList<ArticleData>
    private lateinit var readHistoryList: ArrayList<ReadHistoryData>
    private lateinit var bookmarkedList: ArrayList<ReadHistoryData>
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
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        result = arrayListOf()
        readHistoryList = arrayListOf()
        bookmarkedList = arrayListOf()
        binding.back.setOnClickListener(){
            findNavController().popBackStack()
        }
        binding.title.text = args.topicName
        ViewModel.getFilteredArticle(args.topicName)
        ViewModel.filteredArticle.observe(viewLifecycleOwner, Observer {
            result = it
                ViewModel.readhistory.observe(viewLifecycleOwner, Observer{
                    articleAdapter = ArticleAdapter(requireContext(),result,it,this)
                    binding.recyclerView.adapter = articleAdapter
                })
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(article: ArticleData) {
        val action = ArticleFragmentDirections.actionNavigationArticleToNavigationDetailArticle(article)
        findNavController().navigate(action)
    }

}