package com.example.seaguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.seaguard.fragments.HomeFragment
import com.example.seaguard.fragments.InfoFragment
import com.example.seaguard.fragments.MapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHome = findViewById<Button>(R.id.btn_home)
        val btnMap = findViewById<Button>(R.id.btn_map)
        val btnInfo = findViewById<Button>(R.id.btn_info)
        val btnProfile = findViewById<ImageButton>(R.id.btn_profile)

        loadFragment(HomeFragment())

        btnHome.setOnClickListener {
            loadFragment(HomeFragment())
        }

        btnMap.setOnClickListener {
            loadFragment(MapFragment())
        }

        btnInfo.setOnClickListener {
            loadFragment(InfoFragment())
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
