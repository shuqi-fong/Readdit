package com.example.readdit.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.readdit.R
import com.example.readdit.databinding.FragmentHomeBinding
import com.example.readdit.ui.QuoteData
import com.example.readdit.ui.ViewModel
import com.example.readdit.ui.article.ArticleAdapter
import com.example.readdit.ui.article.ArticleData
import com.example.readdit.ui.article.ReadHistoryData
import com.example.readdit.ui.library.LibraryFragmentDirections
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), ArticleAdapter.OnItemClickListener {

    private lateinit var ViewModel: ViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var historyList: ArrayList<ReadHistoryData>
    private lateinit var articleList: ArrayList<ArticleData>
    private lateinit var result: ArrayList<ArticleData>
    private lateinit var limitedResult: ArrayList<ArticleData>
    private lateinit var recommendList: ArrayList<ArticleData>
    private lateinit var quote: QuoteData
    private lateinit var historyAdapter: ArticleAdapter
    private lateinit var recommendAdapter: ArticleAdapter
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        historyList = arrayListOf()
        articleList = arrayListOf()
        result = arrayListOf()
        limitedResult = arrayListOf()
        recommendList = arrayListOf()

        binding.date.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))

        ViewModel.quote.observe(viewLifecycleOwner, Observer {
            quote = it.random()
            binding.author.text = quote.author
            binding.quote.text = quote.quote
        })

        ViewModel.homehistory.observe(viewLifecycleOwner, Observer {
            historyList = it
            ViewModel.article.observe(viewLifecycleOwner, Observer {
                articleList = it
                result = ViewModel.getFilteredList(historyList,articleList)
                Log.d("kfc", result.size.toString())
                ViewModel.readhistory.observe(viewLifecycleOwner, Observer {
                    historyAdapter = ArticleAdapter(requireContext(),result,it,this)
                    binding.recyclerView1.adapter = historyAdapter
                })
            })
        })

        ViewModel.article.observe(viewLifecycleOwner, Observer {
            for (position in 0 until 2){
                recommendList.add(it[position])
            }
            ViewModel.readhistory.observe(viewLifecycleOwner, Observer {
                recommendAdapter = ArticleAdapter(requireContext(),recommendList,it,this)
                binding.recyclerView2.adapter = recommendAdapter
            })
        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(article: ArticleData) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetailArticle(article.id)
        findNavController().navigate(action)
    }

}