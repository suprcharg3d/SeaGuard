package com.example.seaguard.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.seaguard.R
import com.example.seaguard.data.AppDatabase
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var txtEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        mapView = view.findViewById(R.id.map_all)
        txtEmpty = view.findViewById(R.id.txt_empty)

        mapView.setMultiTouchControls(true)

        val database = AppDatabase.getInstance(requireContext())
        val laporanList = database.laporanDao().getAll()

        val validLaporan = laporanList.filter {
            it.latitude != null && it.longitude != null
        }

        if (validLaporan.isEmpty()) {
            mapView.visibility = View.GONE
            txtEmpty.visibility = View.VISIBLE
            return view
        }

        var avgLat = 0.0
        var avgLng = 0.0

        validLaporan.forEach { laporan ->
            val point = GeoPoint(laporan.latitude!!, laporan.longitude!!)
            avgLat += laporan.latitude!!
            avgLng += laporan.longitude!!

            val marker = Marker(mapView)
            marker.position = point
            marker.title = laporan.lokasi
            marker.subDescription = "Jenis: ${laporan.jenis}"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapView.overlays.add(marker)
        }

        avgLat /= validLaporan.size
        avgLng /= validLaporan.size

        mapView.controller.setZoom(6.0)
        mapView.controller.setCenter(GeoPoint(avgLat, avgLng))

        return view
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
