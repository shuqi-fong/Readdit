package com.example.readdit.ui.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.readdit.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class NotesAdapter(private val context: Context, private val notes: ArrayList<NotesData>, private val listener: OnItemClickListener):
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val notes = notes[position]
        holder.title.text = notes.title
        holder.body.text = notes.body
        holder.lastEdited.text = sdf.format(notes.lastEdited?.toDate())
    }

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{
        val title: TextView = view.findViewById(R.id.noteTitle)
        val body: TextView = view.findViewById(R.id.noteBody)
        val lastEdited: TextView = view.findViewById(R.id.noteLastEdited)

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