package com.example.seaguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.appcompat.app.AlertDialog
import com.example.seaguard.data.AppDatabase
import com.example.seaguard.data.entity.Laporan
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditorActivity : AppCompatActivity() {

    private lateinit var editLokasi: EditText
    private lateinit var editJenis: AutoCompleteTextView
    private lateinit var editDeskripsi: EditText
    private lateinit var btnSave: Button
    private lateinit var btnAddPhoto: Button
    private lateinit var imgPreview: ImageView
    private lateinit var txtLocationStatus: TextView

    private lateinit var database: AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var imageUri: Uri? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var laporanId: Int? = null
    private var laporanLama: Laporan? = null

    companion object {
        private const val REQ_GALLERY = 100
        private const val REQ_CAMERA = 101
        private const val REQ_LOCATION = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        editLokasi = findViewById(R.id.edit_lokasi)
        editJenis = findViewById(R.id.edit_jenis)
        editDeskripsi = findViewById(R.id.edit_deskripsi)
        btnSave = findViewById(R.id.btn_save)
        btnAddPhoto = findViewById(R.id.btn_add_photo)
        imgPreview = findViewById(R.id.img_preview)
        txtLocationStatus = findViewById(R.id.txt_location_status)

        database = AppDatabase.getInstance(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val username = prefs.getString("username", "Unknown") ?: "Unknown"

        val opsiSampah = arrayOf(
            "Plastik", "Kertas", "Logam/Kaleng",
            "Kaca/Beling", "Organik",
            "Jaring Nelayan", "Lainnya"
        )
        editJenis.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, opsiSampah)
        )
        editJenis.setOnClickListener { editJenis.showDropDown() }

        if (intent.hasExtra("id")) {
            laporanId = intent.getIntExtra("id", -1)

            if (laporanId != -1) {
                laporanLama = database.laporanDao().get(laporanId!!)

                laporanLama?.let {
                    editLokasi.setText(it.lokasi)
                    editJenis.setText(it.jenis, false)
                    editDeskripsi.setText(it.deskripsi)

                    if (!it.photoPath.isNullOrEmpty()) {
                        imageUri = Uri.parse(it.photoPath)
                        imgPreview.setImageURI(imageUri)
                    }

                    latitude = it.latitude
                    longitude = it.longitude

                    if (latitude != null && longitude != null) {
                        txtLocationStatus.text =
                            "📍 Lokasi terekam\nLat: %.5f, Lng: %.5f"
                                .format(latitude, longitude)
                    }
                }
            }
        }

        btnAddPhoto.setOnClickListener {
            showImageChooser()
        }

        getLocation()

        btnSave.setOnClickListener {
            if (editLokasi.text.isNotEmpty() && editJenis.text.isNotEmpty()) {

                if (laporanId != null) {
                    database.laporanDao().update(
                        Laporan(
                            laporanId,
                            editLokasi.text.toString(),
                            editJenis.text.toString(),
                            editDeskripsi.text.toString(),
                            laporanLama?.dilaporkanOleh ?: username,
                            imageUri?.toString(),
                            latitude,
                            longitude
                        )
                    )
                } else {
                    database.laporanDao().insertAll(
                        Laporan(
                            null,
                            editLokasi.text.toString(),
                            editJenis.text.toString(),
                            editDeskripsi.text.toString(),
                            username,
                            imageUri?.toString(),
                            latitude,
                            longitude
                        )
                    )
                }
                finish()
            } else {
                Toast.makeText(this, "Lengkapi data dulu ya!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageChooser() {
        val options = arrayOf("Kamera", "Galeri")

        AlertDialog.Builder(this)
            .setTitle("Pilih Sumber Foto")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQ_GALLERY)
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                300
            )
            return
        }

        val photoFile = createImageFile()
        imageUri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQ_CAMERA)
    }

    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_$timeStamp", ".jpg", storageDir)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOCATION
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                txtLocationStatus.text =
                    "📍 Lokasi terekam\nLat: %.5f, Lng: %.5f"
                        .format(latitude, longitude)
                txtLocationStatus.textSize = 14f
            } else {
                txtLocationStatus.text = "📍 Lokasi tidak tersedia"
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        when (requestCode) {
            REQ_GALLERY -> {
                imageUri = data?.data
                imgPreview.setImageURI(imageUri)
            }
            REQ_CAMERA -> {
                imgPreview.setImageURI(imageUri)
            }
        }
    }
}