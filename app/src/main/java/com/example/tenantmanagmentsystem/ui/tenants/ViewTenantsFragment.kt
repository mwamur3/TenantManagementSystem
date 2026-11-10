
package com.example.tenantmanagmentsystem.ui.tenants

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.tenantmanagmentsystem.data.repository.TenantRepository
import com.example.tenantmanagmentsystem.databinding.FragmentViewTenantsBinding

class ViewTenantsFragment : Fragment() {

    private var _binding: FragmentViewTenantsBinding? = null
    private val binding get() = _binding!!

    private val args: ViewTenantsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewTenantsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newTenant = args.newTenant
        binding.tenant = newTenant

        if (newTenant != null) {
            binding.newTenantGroup.visibility = View.VISIBLE
            binding.newTenantTextView.text = newTenant.summary()

            binding.callButton.setOnClickListener {
                if (newTenant.phone.isNotBlank()) {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${newTenant.phone}")
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Tenant phone number is missing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            binding.newTenantGroup.visibility = View.GONE
        }

        val tenants = TenantRepository.tenants

        binding.allTenantsTextView.text =
            if (tenants.isEmpty()) {
                "No tenants yet. Add one from the Dashboard."
            } else {
                tenants.joinToString("\n\n") { it.summary() }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
