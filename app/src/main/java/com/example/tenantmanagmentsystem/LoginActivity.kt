package com.example.tenantmanagmentsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagmentsystem.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive email from RegisterActivity
        val registeredEmail = intent.getStringExtra("EMAIL")

        if (registeredEmail != null) {
            binding.emailEditText.setText(registeredEmail)
        }

        // LOGIN button
        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter your email and password",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // Explicit Intent to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Register link
        binding.registerTextView.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Help website
        binding.helpTextView.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.strathmore.edu")
            )

            startActivity(intent)
        }
    }
}