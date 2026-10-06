package com.example.tenantmanagmentsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagmentsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Stores the most recently saved tenant
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // SAVE TENANT
        binding.saveButton.setOnClickListener {

            val name = binding.tenantNameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val rent = binding.rentEditText.text.toString()

            if (name.isEmpty()) {
                binding.tenantNameEditText.error =
                    "Please enter tenant name"
                return@setOnClickListener
            }

            // Create a Tenant object
            val tenant = Tenant(name, phone, rent)

            // Display the tenant using Data Binding
            binding.tenant = tenant

            // Remember the last saved tenant
            lastTenant = tenant

            binding.tenantStatusTextView.text =
                "Tenant saved successfully"

            // Clear the input fields
            binding.tenantNameEditText.text.clear()
            binding.phoneEditText.text.clear()
            binding.rentEditText.text.clear()
        }

        // CALL TENANT
        binding.callButton.setOnClickListener {

            val tenant = lastTenant

            if (tenant == null) {
                Toast.makeText(
                    this,
                    "Save a tenant first",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Open the phone dialer with the tenant's number
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:${tenant.phone}")
            )

            startActivity(intent)
        }
    }
}