package com.example.readdit.ui.explore

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

class ExploreAdapter(private val context: Context, private val urls: List<String>): RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        return ExploreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_explore, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val menu = urls[position]
        Log.d("kfc","$menu")
        Glide.with(context)
            .load(menu)
            .into(holder.menuImage)
    }

    class ExploreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuImage: ImageView = view.findViewById(R.id.image_explore)
    }
}