package com.example.readdit.ui.article

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readdit.R
import com.example.readdit.ui.explore.ExploreAdapter

class ArticleAdapter(private val context: Context, private val article: ArrayList<ArticleData>,private val listener: ArticleAdapter.OnItemClickListener): RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

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
        val article = article[position]
        holder.datePosted.text = article.datePosted
        holder.description.text = article.description
        holder.duration.text = article.duration
        holder.readCount.text = article.readCount
        holder.title.text = article.title
        holder.topic.text = article.topic
        Glide.with(context)
            .load(article.coverImage)
            .into(holder.coverImage)
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        val coverImage: ImageView = view.findViewById(R.id.article_coverImage)
        val datePosted: TextView = view.findViewById(R.id.article_datePosted)
        val description: TextView = view.findViewById(R.id.article_description)
        val duration: TextView = view.findViewById(R.id.article_duration)
        val readCount: TextView = view.findViewById(R.id.article_readCount)
        val title: TextView = view.findViewById(R.id.article_title)
        val topic: TextView = view.findViewById(R.id.article_topic)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}