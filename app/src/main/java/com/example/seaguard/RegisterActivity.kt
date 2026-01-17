package com.example.seaguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seaguard.data.AppDatabase
import com.example.seaguard.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        val userDao = AppDatabase.getInstance(this).userDao()

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val existingUser = userDao.checkUser(username)

                runOnUiThread {
                    if (existingUser != null) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Username already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insert(User(username = username, password = password))
                        }
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }

        // 🔁 Back to Login
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
