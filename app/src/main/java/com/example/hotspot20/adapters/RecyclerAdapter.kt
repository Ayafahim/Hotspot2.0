package com.example.hotspot20.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hotspot20.R

class RecyclerAdapter() : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val titles = arrayOf(
        "Emin",
        "Saif",
        "Jawa",
        "Aya",
        "Jonas",
        "Thomas",
        "Liv",
        "Hanna",
        "Mohammed",
        "Caroline",
        "Natalie",
        "Mina",
        "Jesper"
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.name)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val context = itemView.context
                Toast.makeText(
                    itemView.context,
                    "Du har trykket på chat # ${position + 1}",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]

    }

}