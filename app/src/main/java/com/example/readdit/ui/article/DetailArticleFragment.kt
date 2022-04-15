package com.example.readdit.ui.article

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.readdit.databinding.DetailItemArticleBinding
import com.example.readdit.ui.ViewModel
import com.google.firebase.firestore.*
import java.text.SimpleDateFormat

class DetailArticleFragment : Fragment() {

    private lateinit var ViewModel: ViewModel
   private lateinit var article: ArticleData
    private var _binding: DetailItemArticleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: DetailArticleFragmentArgs by navArgs()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = ViewModelProvider(this).get(com.example.readdit.ui.ViewModel::class.java)

        _binding = DetailItemArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ViewModel.getDetailArticle(args.articleID) {
            article = it
            ViewModel.readhistory.observe(viewLifecycleOwner, Observer{
                val articleID = article.id
                Glide.with(requireContext()).load(article.coverImage).into(binding.profileImage)
                binding.title.text = article.title
                binding.readCount.text = article.readCount.toString()
                if (article.body.contains("_n")){
                    val newbody = article.body.replace("_n","\n")
                    binding.body.text = newbody
                }
                binding.date.text = sdf.format(article.datePosted?.toDate())
                binding.topic.text = article.topic
                for (readhistory in it) {
                    if (articleID == readhistory.article && readhistory.isBookmarked) {
                        binding.articleBookmark.isChecked = true
                    }
                }
                val readhistoryID = ViewModel.checkReadHistory(it,articleID)
                if (readhistoryID != null){
                    ViewModel.updateReadHistory(readhistoryID)
                }
                else{
                    ViewModel.addReadHistory(articleID)
                }

                binding.articleBookmark.setOnCheckedChangeListener { CheckBox, isChecked ->
                    var db = FirebaseFirestore.getInstance()

                    val check = checkReadHistory(articleID,it)
                    if (check){
                        for (readhistory in it) {
                            if (!isChecked) {
                                if (articleID == readhistory.article && readhistory.isBookmarked) {
                                    db.collection("read_history").document(readhistory.id)
                                        .update("isBookmarked", false)
                                }
                            } else {
                                if (articleID == readhistory.article && !readhistory.isBookmarked) {
                                    db.collection("read_history").document(readhistory.id)
                                        .update("isBookmarked", true)
                                }
                            }
                        }
                    }
                    else{
                        val data = hashMapOf(
                            "article" to articleID,
                            "isBookmarked" to true,
                            "isRead" to false,
                            "readDate" to FieldValue.serverTimestamp(),
                            "user" to "user001"
                        )
                        db.collection("read_history").add(data)
                    }
                }
            })
        }

        return root
    }

    fun checkReadHistory(articleID : String, readHistory: ArrayList<ReadHistoryData>): Boolean {
        for (readhistory in readHistory) {
            if (articleID == readhistory.article) {
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}