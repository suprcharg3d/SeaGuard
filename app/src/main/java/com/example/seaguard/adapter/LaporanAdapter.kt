package com.example.seaguard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seaguard.DetailLaporanActivity
import com.example.seaguard.R
import com.example.seaguard.data.entity.Laporan

class LaporanAdapter(
    private var list: List<Laporan>
) : RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lokasi: TextView = view.findViewById(R.id.txt_lokasi)
        var jenis: TextView = view.findViewById(R.id.txt_jenis)
        var deskripsi: TextView = view.findViewById(R.id.txt_deskripsi)
        var pelapor: TextView = view.findViewById(R.id.txt_pelapor)

        init {
            view.setOnClickListener {
                val laporan = list[adapterPosition]
                val intent = Intent(view.context, DetailLaporanActivity::class.java)
                intent.putExtra("id", laporan.uid)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_laporan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val laporan = list[position]
        holder.lokasi.text = laporan.lokasi
        holder.jenis.text = laporan.jenis
        holder.deskripsi.text = laporan.deskripsi
        holder.pelapor.text =
            "Dilaporkan oleh: ${laporan.dilaporkanOleh ?: "Unknown"}"
    }

    override fun getItemCount(): Int = list.size
}