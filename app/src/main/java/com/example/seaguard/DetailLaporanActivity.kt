package com.example.seaguard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seaguard.data.AppDatabase
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class DetailLaporanActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_laporan)

        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )

        val txtLokasi = findViewById<TextView>(R.id.txt_lokasi)
        val txtJenis = findViewById<TextView>(R.id.txt_jenis)
        val txtDeskripsi = findViewById<TextView>(R.id.txt_deskripsi)
        val txtPelapor = findViewById<TextView>(R.id.txt_pelapor)
        val txtKoordinat = findViewById<TextView>(R.id.txt_koordinat)
        val imgFoto = findViewById<ImageView>(R.id.img_foto)
        val btnEdit = findViewById<Button>(R.id.btn_edit)
        val btnHapus = findViewById<Button>(R.id.btn_hapus)
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnMapExternal = findViewById<Button>(R.id.btn_map_external)
        mapView = findViewById(R.id.map)

        val id = intent.getIntExtra("id", -1)
        val database = AppDatabase.getInstance(this)
        val laporan = database.laporanDao().get(id)

        txtLokasi.text = laporan.lokasi
        txtJenis.text = laporan.jenis
        txtDeskripsi.text = laporan.deskripsi
        txtPelapor.text = "Dilaporkan oleh: ${laporan.dilaporkanOleh ?: "Unknown"}"

        if (laporan.latitude != null && laporan.longitude != null) {

            txtKoordinat.text =
                "📍 ${laporan.latitude}, ${laporan.longitude}"

            mapView.setMultiTouchControls(true)

            val point = GeoPoint(laporan.latitude!!, laporan.longitude!!)
            mapView.controller.setZoom(16.0)
            mapView.controller.setCenter(point)

            val marker = Marker(mapView)
            marker.position = point
            marker.title = "Lokasi Sampah"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapView.overlays.add(marker)

        } else {
            txtKoordinat.text = "📍 Lokasi tidak tersedia"
            mapView.visibility = View.GONE
        }

        if (!laporan.photoPath.isNullOrEmpty()) {
            imgFoto.setImageURI(Uri.parse(laporan.photoPath))

            imgFoto.setOnClickListener {
                val intent = Intent(this, ImagePreviewActivity::class.java)
                intent.putExtra("image", laporan.photoPath)
                startActivity(intent)
            }
        } else {
            imgFoto.visibility = View.GONE
        }

        btnMapExternal.setOnClickListener {
            if (laporan.latitude != null && laporan.longitude != null) {
                val uri = Uri.parse(
                    "geo:${laporan.latitude},${laporan.longitude}?q=${laporan.latitude},${laporan.longitude}"
                )
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            intent.putExtra("id", laporan.uid)
            startActivity(intent)
        }

        btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus laporan?")
                .setMessage("Data ini akan dihapus permanen.")
                .setPositiveButton("Hapus") { _, _ ->
                    database.laporanDao().delete(laporan)
                    finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        btnBack.setOnClickListener {
            finish()
        }
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