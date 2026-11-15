
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
            inflater, container, false
        )

        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onViewCreated")

        tenantViewModel.tenants.observe(viewLifecycleOwner) { tenants ->
            binding.tenantCountTextView.text =
                "Tenants\n${tenants.size}"
        }

        tenantViewModel.totalRent.observe(viewLifecycleOwner) { totalRent ->
            binding.totalRentTextView.text =
                "Total rent\nKSh %,.0f".format(totalRent)
        }

        binding.addTenantButton.setOnClickListener {
            findNavController().navigate(R.id.addTenantFragment)
        }

        binding.viewTenantsButton.setOnClickListener {
            findNavController().navigate(R.id.viewTenantsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TENANT_LIFECYCLE", "DashboardFragment: onDestroyView")
        _binding = null
    }
}
