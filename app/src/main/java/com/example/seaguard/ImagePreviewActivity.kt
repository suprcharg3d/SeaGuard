package com.example.seaguard

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val img = findViewById<ImageView>(R.id.img_full)
        val uri = intent.getStringExtra("image")

        if (uri != null) {
            img.setImageURI(Uri.parse(uri))
        }

        img.setOnClickListener { finish() }
    }
}
