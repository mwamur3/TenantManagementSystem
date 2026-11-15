package com.example.tenantmanagmentsystem.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tenantmanagmentsystem.R
import com.example.tenantmanagmentsystem.databinding.FragmentDashboardBinding
import com.example.tenantmanagmentsystem.ui.viewmodel.TenantViewModel
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val tenantViewModel: TenantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(
            inflater,
            container,
            false
        )

        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onViewCreated")

        // Observe registered tenants
        tenantViewModel.tenants.observe(viewLifecycleOwner) { tenants ->

            binding.tenantCountTextView.text =
                "Tenants\n${tenants.size}"

            // Calculate total monthly rent
            val totalRent = tenants.sumOf { tenant ->

                tenant.rent
                    .replace(",", "")
                    .trim()
                    .toDoubleOrNull() ?: 0.0
            }

            binding.totalRentTextView.text = String.format(
                Locale.US,
                "Total Monthly Rent\nKSh %,.2f",
                totalRent
            )
        }

        // Navigate to Add Tenant
        binding.addTenantButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_addTenantFragment
            )
        }

        // Navigate to View Tenants
        binding.viewTenantsButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_viewTenantsFragment
            )
        }

        // Navigate to Reports
        binding.reportsButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_dashboardFragment_to_reportsFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onDestroyView")

        _binding = null
    }
}