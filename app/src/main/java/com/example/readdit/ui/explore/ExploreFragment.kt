package com.example.readdit.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readdit.databinding.FragmentExploreBinding
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.article.ArticleAdapter
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.article.ReadHistoryData
import com.google.firebase.firestore.FirebaseFirestore


class ExploreFragment : Fragment(),ExploreAdapter.OnItemClickListener,ArticleAdapter.OnItemClickListener{

    private lateinit var db: FirebaseFirestore
    private lateinit var exploreList: ArrayList<ExploreData>
    private lateinit var articleList: ArrayList<ArticleData>
    private lateinit var readHistoryList: ArrayList<ReadHistoryData>
    private lateinit var filteredArticleList: ArrayList<ArticleData>
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var ViewModel: ViewModel
    private var _binding: FragmentExploreBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        exploreList = arrayListOf()
        articleList = arrayListOf()
        readHistoryList = arrayListOf()

        search()
        return root
    }

    fun search(){
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(content: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(content: String?): Boolean {
                if (content.isNullOrEmpty()){
                    getExploreData()
                }else{
                    getArticleData()
                    filter(content)
                }
                return false
            }
        })
    }

    fun getExploreData(){
        ViewModel.explore.observe(viewLifecycleOwner, Observer{
            exploreList = it
            exploreAdapter = ExploreAdapter(requireContext(),exploreList,this)
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.recyclerView.adapter = exploreAdapter
        })
    }

    private fun getArticleData() {
        ViewModel.article.observe(viewLifecycleOwner, Observer{
            articleList = it
            articleAdapter = ArticleAdapter(requireContext(),articleList,readHistoryList,this)
            binding.recyclerView.adapter = articleAdapter
        })
        Log.d("kfc",articleList.toString())
    }

    private fun getReadHistoryData() {
        ViewModel.readhistory.observe(viewLifecycleOwner, Observer{
            readHistoryList = it
        })
    }

    private fun filter(text: String) {
        getReadHistoryData()
        filteredArticleList = arrayListOf()
        articleAdapter = ArticleAdapter(requireContext(),filteredArticleList,readHistoryList,this)
        //looping through existing elements
        for (article in articleList) {
            //if the existing elements contains the search input
            if (article.title.lowercase().contains(text.lowercase())) {
                //adding the element to filtered list
                filteredArticleList.add(article)
            }
        }
        //calling a method of the adapter class and passing the filtered list
        binding.recyclerView.adapter = articleAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        articleAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val topicName = exploreList[position].topicName
        val action = ExploreFragmentDirections.actionNavigationExploreToNavigationArticle(topicName)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(article: ArticleData) {
        val action = ExploreFragmentDirections.actionNavigationExploreToNavigationDetailArticle(article)
        findNavController().navigate(action)
    }

}