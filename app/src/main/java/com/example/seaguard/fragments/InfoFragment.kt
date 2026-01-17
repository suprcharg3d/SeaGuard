package com.example.seaguard.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seaguard.R
import com.example.seaguard.adapter.InfoAdapter

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_info)

        val titles = listOf(
            "Kurangi plastik sekali pakai",
            "Jangan membuang puntung rokok",
            "Bawa tas belanja sendiri",
            "Ikuti kegiatan bersih pantai",
            "Daur ulang sampah rumah tangga"
        )

        val subtitles = listOf(
            "Hindari sedotan dan alat makan plastik.",
            "Puntung rokok adalah sampah laut terbanyak.",
            "Gunakan tas kain yang bisa dipakai ulang.",
            "Gabung dengan komunitas SeaGuard lokal.",
            "Pilah sampahmu sebelum dibuang."
        )

        val details = listOf(
            "Plastik sekali pakai seperti sedotan dan kantong plastik dapat mencemari laut dan membahayakan biota laut.",
            "Puntung rokok mengandung zat kimia berbahaya yang mencemari laut.",
            "Tas kain membantu mengurangi sampah plastik secara signifikan.",
            "Kegiatan bersih pantai membantu menjaga ekosistem pesisir.",
            "Daur ulang membantu mengurangi beban TPA dan pencemaran laut."
        )

        val images = listOf(
            R.drawable.tip_plastik,
            R.drawable.tip_rokok,
            R.drawable.tip_tas,
            R.drawable.tip_bersih_pantai,
            R.drawable.tip_daur_ulang
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = InfoAdapter(
            titles,
            subtitles,
            details,
            images
        ) { title, detail, image ->
            val dialogView = layoutInflater.inflate(R.layout.dialog_info_detail, null)
            dialogView.findViewById<android.widget.ImageView>(R.id.img_detail)
                .setImageResource(image)
            dialogView.findViewById<android.widget.TextView>(R.id.txt_detail)
                .text = detail

            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("Tutup", null)
                .show()
        }

        return view
    }
}
