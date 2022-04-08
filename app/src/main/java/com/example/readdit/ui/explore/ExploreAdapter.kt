package com.example.readdit.ui.explore

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util.getSnapshot
import com.example.readdit.R
import com.google.firebase.firestore.DocumentSnapshot

class ExploreAdapter(private val context: Context, private val topic: ArrayList<ExploreData>,private val listener: OnItemClickListener): RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    override fun getItemCount(): Int {
        return topic.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_explore, parent, false)
        return ExploreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val topic = topic[position]
        Log.d("kfc","$topic")
        Glide.with(context)
            .load(topic.topicImage)
            .into(holder.topicImage)
    }

    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        val topicImage: ImageView = itemView.findViewById(R.id.explore_image)

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