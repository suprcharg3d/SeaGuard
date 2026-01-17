package com.example.seaguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val imgProfile = findViewById<ImageView>(R.id.img_profile)
        val txtUsername = findViewById<TextView>(R.id.txt_username)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val username = prefs.getString("username", "Unknown")

        txtUsername.text = username

        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
