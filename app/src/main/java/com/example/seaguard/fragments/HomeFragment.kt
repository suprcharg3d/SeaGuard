package com.example.seaguard.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seaguard.EditorActivity
import com.example.seaguard.R
import com.example.seaguard.adapter.LaporanAdapter
import com.example.seaguard.data.AppDatabase
import com.example.seaguard.data.entity.Laporan
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: LaporanAdapter
    private lateinit var database: AppDatabase
    private val list = mutableListOf<Laporan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        fab = view.findViewById(R.id.fab)
        database = AppDatabase.getInstance(requireContext())

        adapter = LaporanAdapter(list)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(requireContext(), EditorActivity::class.java))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        list.clear()
        list.addAll(database.laporanDao().getAll())
        adapter.notifyDataSetChanged()
    }
}
