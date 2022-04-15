package com.example.readdit.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readdit.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class ArticleAdapter(
    private val context: Context,
    private val article: ArrayList<ArticleData>,
    private val readHistory: ArrayList<ReadHistoryData>,
    private val listener: ArticleAdapter.OnItemClickListener
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun getItemCount(): Int {
        return article.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val article = article[position]
        holder.datePosted.text = sdf.format(article.datePosted?.toDate())
        holder.duration.text = article.durationMin.toString() + " mins"
        holder.readCount.text = article.readCount.toString() + " reads"
        holder.title.text = article.title
        holder.topic.text = article.topic
        Glide.with(context)
            .load(article.coverImage)
            .into(holder.coverImage)

        for (readhistory in readHistory) {
            if (article.id == readhistory.article && readhistory.isBookmarked) {
                holder.bookmark.isChecked = true
            }
        }

        holder.bookmark.setOnCheckedChangeListener { CheckBox, isChecked ->
            var db = FirebaseFirestore.getInstance()

            val check = checkReadHistory(article.id)
            if (check){
                for (readhistory in readHistory) {
                    if (!isChecked) {
                        if (article.id == readhistory.article && readhistory.isBookmarked) {
                            db.collection("read_history").document(readhistory.id)
                                .update("isBookmarked", false)
                        }
                    } else {
                        if (article.id == readhistory.article && !readhistory.isBookmarked) {
                            db.collection("read_history").document(readhistory.id)
                                .update("isBookmarked", true)
                        }
                    }
                }
            }
            else{
                val data = hashMapOf(
                    "article" to article.id,
                    "isBookmarked" to true,
                    "isRead" to false,
                    "readDate" to FieldValue.serverTimestamp(),
                    "user" to "user001"
                )
                db.collection("read_history").add(data)
            }
        }
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val coverImage: ImageView = view.findViewById(R.id.article_coverImage)
        val datePosted: TextView = view.findViewById(R.id.article_datePosted)
        val duration: TextView = view.findViewById(R.id.article_duration)
        val readCount: TextView = view.findViewById(R.id.article_readCount)
        val title: TextView = view.findViewById(R.id.article_title)
        val topic: TextView = view.findViewById(R.id.article_topic)
        val bookmark: CheckBox = view.findViewById(R.id.article_bookmark)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(article[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(article: ArticleData)
    }

    fun checkReadHistory(articleID : String): Boolean {
        for (readhistory in readHistory) {
            if (articleID == readhistory.article) {
                return true
            }
        }
        return false
    }
}