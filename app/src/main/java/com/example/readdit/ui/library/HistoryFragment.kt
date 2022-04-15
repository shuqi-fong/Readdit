package com.example.readdit.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.readdit.databinding.FragmentHistoryBinding
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.article.ArticleAdapter
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.article.ArticleFragmentDirections
import com.example.readdit.ui.article.ReadHistoryData
import kotlinx.android.synthetic.main.fragment_home.*

class HistoryFragment : Fragment(), ArticleAdapter.OnItemClickListener {

    private lateinit var ViewModel: ViewModel
    private lateinit var readHistoryList: ArrayList<ReadHistoryData>
    private lateinit var bookmarkedList: ArrayList<ReadHistoryData>
    private lateinit var articleList: ArrayList<ArticleData>
    private lateinit var historyList: ArrayList<ReadHistoryData>
    private lateinit var result: ArrayList<ArticleData>
    private lateinit var articleAdapter: ArticleAdapter
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        articleList = arrayListOf()
        historyList = arrayListOf()
        readHistoryList = arrayListOf()
        bookmarkedList = arrayListOf()
        result = arrayListOf()

        ViewModel.history.observe(viewLifecycleOwner, Observer {
            historyList = it
            ViewModel.article.observe(viewLifecycleOwner, Observer {
                articleList = it
                result = ViewModel.getFilteredList(historyList,articleList)
                ViewModel.readhistory.observe(viewLifecycleOwner, Observer {
                    Log.d("kfc",result.toString())
                    if(it.size == 0) {
                        binding.noDataText.visibility = View.VISIBLE
                    } else {
                        binding.noDataText.visibility = View.INVISIBLE
                    }
                    articleAdapter = ArticleAdapter(requireContext(),result,it,this)
                    binding.recyclerView.adapter = articleAdapter
                })
           })
      })
        return root
    }

    override fun onItemClick(article: ArticleData) {
        val action = LibraryFragmentDirections.actionNavigationLibraryToNavigationDetailArticle(article)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}