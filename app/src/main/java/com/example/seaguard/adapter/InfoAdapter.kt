package com.example.seaguard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seaguard.R

class InfoAdapter(
    private val titles: List<String>,
    private val subtitles: List<String>,
    private val details: List<String>,
    private val images: List<Int>,
    private val onClick: (String, String, Int) -> Unit
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txt_title)
        val desc: TextView = view.findViewById(R.id.txt_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titles[position]
        holder.desc.text = subtitles[position]

        holder.itemView.setOnClickListener {
            onClick(
                titles[position],
                details[position],
                images[position]
            )
        }
    }

    override fun getItemCount() = titles.size
}
