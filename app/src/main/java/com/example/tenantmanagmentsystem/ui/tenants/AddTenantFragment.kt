package com.example.tenantmanagmentsystem.ui.tenants

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.tenantmanagmentsystem.R
import com.example.tenantmanagmentsystem.data.model.Tenant
import com.example.tenantmanagmentsystem.databinding.FragmentAddTenantBinding
import com.example.tenantmanagmentsystem.ui.viewmodel.TenantViewModel

class AddTenantFragment : Fragment() {

    private var _binding: FragmentAddTenantBinding? = null
    private val binding get() = _binding!!

    private val tenantViewModel: TenantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTenantBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TENANT_LIFECYCLE", "AddTenantFragment: onViewCreated")

        binding.saveButton.setOnClickListener {

            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rentText = binding.rentEditText.text.toString().trim()

            // Validate required fields
            if (name.isEmpty() || phone.isEmpty() || rentText.isEmpty()) {
                showMessage("Please fill in all fields")
                return@setOnClickListener
            }

            // Validate tenant name
            if (name.length < 2) {
                showMessage("Enter a valid tenant name")
                return@setOnClickListener
            }

            // Validate Kenyan phone number
            val phonePattern = Regex("^(?:\\+254|254|0)(?:7|1)\\d{8}$")

            if (!phonePattern.matches(phone)) {
                showMessage("Enter a valid Kenyan phone number")
                return@setOnClickListener
            }

            // Validate rent
            val rent = rentText.replace(",", "").toDoubleOrNull()

            if (rent == null || rent <= 0) {
                showMessage("Enter a valid positive rent amount")
                return@setOnClickListener
            }

            val tenant = Tenant(
                name = name,
                phone = phone,
                rent = rent.toString()
            )

            // Prevent repeated submissions
            binding.saveButton.isEnabled = false

            tenantViewModel.addTenant(
                tenant,
                onComplete = {

                    if (isAdded) {

                        Toast.makeText(
                            requireContext(),
                            "Tenant saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val bundle = bundleOf("newTenant" to tenant)

                        findNavController().navigate(
                            R.id.action_addTenantFragment_to_viewTenantsFragment,
                            bundle
                        )
                    }
                },
                onError = { error ->

                    if (isAdded) {

                        binding.saveButton.isEnabled = true

                        showMessage("Could not save tenant: $error")
                    }
                }
            )
        }

        binding.callButton.setOnClickListener {

            val phone = binding.phoneEditText.text.toString().trim()

            if (phone.isEmpty()) {

                showMessage("Enter a phone number first")

            } else {

                val phonePattern = Regex("^(?:\\+254|254|0)(?:7|1)\\d{8}$")

                if (phonePattern.matches(phone)) {

                    startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$phone")
                        )
                    )

                } else {

                    showMessage("Enter a valid Kenyan phone number")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("TENANT_LIFECYCLE", "AddTenantFragment: onDestroyView")

        _binding = null
    }
}