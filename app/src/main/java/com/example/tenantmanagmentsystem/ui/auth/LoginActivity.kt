
package com.example.tenantmanagmentsystem.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagmentsystem.databinding.ActivityLoginBinding
import com.example.tenantmanagmentsystem.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registeredEmail = intent.getStringExtra("EMAIL")

        if (registeredEmail != null) {
            binding.emailEditText.setText(registeredEmail)
        }

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
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("EMAIL", email)
                startActivity(mainIntent)
                finish()
            }
        }

        binding.registerTextView.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        binding.helpTextView.setOnClickListener {
            val helpIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.strathmore.edu")
            )
            startActivity(helpIntent)
        }
    }
}
