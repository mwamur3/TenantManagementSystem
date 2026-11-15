package com.example.tenantmanagmentsystem.ui.tenants

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.tenantmanagmentsystem.data.model.Tenant
import com.example.tenantmanagmentsystem.databinding.FragmentViewTenantsBinding
import com.example.tenantmanagmentsystem.ui.viewmodel.TenantViewModel

class ViewTenantsFragment : Fragment() {

    private var _binding: FragmentViewTenantsBinding? = null
    private val binding get() = _binding!!

    private val args: ViewTenantsFragmentArgs by navArgs()

    private val tenantViewModel: TenantViewModel by activityViewModels()

    private var allTenants: List<Tenant> = emptyList()

    private lateinit var searchInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewTenantsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchField()

        val newTenant = args.newTenant

        binding.tenant = newTenant

        if (newTenant != null) {

            binding.newTenantGroup.visibility = View.VISIBLE

            binding.newTenantTextView.text = newTenant.summary()

            binding.callButton.setOnClickListener {
                callTenant(newTenant.phone)
            }

        } else {

            binding.newTenantGroup.visibility = View.GONE
        }

        tenantViewModel.tenants.observe(viewLifecycleOwner) { tenants ->

            allTenants = tenants

            filterTenants(searchInput.text.toString())
        }
    }

    private fun setupSearchField() {

        searchInput = EditText(requireContext()).apply {

            hint = "Search by tenant name, phone or ID"

            inputType = InputType.TYPE_CLASS_TEXT

            textSize = 15f

            setSingleLine(true)

            setPadding(20, 18, 20, 18)

            setBackgroundColor(0xFFFFFFFF.toInt())
        }

        val searchParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 12, 0, 16)
        }

        // Add the search field inside the existing parent LinearLayout.
        // This avoids adding a second direct child to the ScrollView.

        val parent = binding.tenantListContainer.parent as? LinearLayout

        if (parent != null) {

            val position = parent.indexOfChild(binding.tenantListContainer)

            parent.addView(searchInput, position, searchParams)

        } else {

            Log.e(
                "TENANT_SEARCH",
                "Could not find the tenant list's LinearLayout parent."
            )
        }

        searchInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                filterTenants(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterTenants(query: String) {

        val searchQuery = query.trim()

        val filteredTenants = if (searchQuery.isEmpty()) {

            allTenants

        } else {

            allTenants.filter { tenant ->

                tenant.name.contains(searchQuery, ignoreCase = true) ||
                        tenant.phone.contains(searchQuery, ignoreCase = true) ||
                        tenant.id.toString().contains(searchQuery, ignoreCase = true)
            }
        }

        displayTenants(filteredTenants, searchQuery.isNotEmpty())
    }

    private fun displayTenants(
        tenants: List<Tenant>,
        isSearching: Boolean
    ) {

        binding.tenantListContainer.removeAllViews()

        if (tenants.isEmpty()) {

            val emptyMessage = TextView(requireContext()).apply {

                text = if (isSearching) {
                    "No tenants match your search."
                } else {
                    "No tenants registered yet."
                }

                textSize = 16f

                setPadding(12, 20, 12, 20)
            }

            binding.tenantListContainer.addView(emptyMessage)

        } else {

            tenants.forEach { tenant ->

                addTenantCard(tenant)
            }
        }
    }

    private fun addTenantCard(tenant: Tenant) {

        val context = requireContext()

        val card = LinearLayout(context).apply {

            orientation = LinearLayout.VERTICAL

            setPadding(16, 16, 16, 16)

            setBackgroundColor(0xFFE3F2FD.toInt())
        }

        val cardParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {

            bottomMargin = 16
        }

        val details = TextView(context).apply {

            text = tenant.summary()

            textSize = 16f

            setTextColor(0xFF000000.toInt())
        }

        card.addView(details)

        val buttonsLayout = LinearLayout(context).apply {

            orientation = LinearLayout.HORIZONTAL

            gravity = Gravity.CENTER_VERTICAL
        }

        val callButton = Button(context).apply {

            text = "CALL"

            setOnClickListener {
                callTenant(tenant.phone)
            }
        }

        val editButton = Button(context).apply {

            text = "EDIT"

            setOnClickListener {
                showEditDialog(tenant)
            }
        }

        val deleteButton = Button(context).apply {

            text = "DELETE"

            setOnClickListener {
                showDeleteConfirmation(tenant)
            }
        }

        buttonsLayout.addView(callButton)

        buttonsLayout.addView(editButton)

        buttonsLayout.addView(deleteButton)

        card.addView(buttonsLayout)

        binding.tenantListContainer.addView(card, cardParams)
    }

    private fun callTenant(phone: String) {

        if (phone.isNotBlank()) {

            startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$phone")
                )
            )

        } else {

            showMessage("Tenant phone number is missing")
        }
    }

    private fun showEditDialog(tenant: Tenant) {

        val context = requireContext()

        val layout = LinearLayout(context).apply {

            orientation = LinearLayout.VERTICAL

            setPadding(40, 16, 40, 8)
        }

        val nameInput = EditText(context).apply {

            hint = "Tenant name"

            setText(tenant.name)
        }

        val phoneInput = EditText(context).apply {

            hint = "Phone number"

            inputType = InputType.TYPE_CLASS_PHONE

            setText(tenant.phone)
        }

        val rentInput = EditText(context).apply {

            hint = "Monthly rent"

            inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL

            setText(tenant.rent)
        }

        layout.addView(nameInput)

        layout.addView(phoneInput)

        layout.addView(rentInput)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit Tenant Details")
            .setView(layout)
            .setPositiveButton("SAVE", null)
            .setNegativeButton("CANCEL", null)
            .create()

        dialog.setOnShowListener {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {

                    val name = nameInput.text.toString().trim()

                    val phone = phoneInput.text.toString().trim()

                    val rent = rentInput.text.toString().trim()

                    val phonePattern =
                        Regex("^(?:\\+254|254|0)(?:7|1)\\d{8}$")

                    val rentValue =
                        rent.replace(",", "").toDoubleOrNull()

                    when {

                        name.length < 2 -> {

                            showMessage("Enter a valid tenant name")
                        }

                        !phonePattern.matches(phone) -> {

                            showMessage("Enter a valid Kenyan phone number")
                        }

                        rentValue == null || rentValue <= 0 -> {

                            showMessage("Enter a valid positive rent amount")
                        }

                        else -> {

                            val updatedTenant = Tenant(
                                name = name,
                                phone = phone,
                                rent = rentValue.toString(),
                                id = tenant.id
                            )

                            tenantViewModel.updateTenant(
                                updatedTenant,

                                onComplete = {

                                    if (isAdded) {
                                        showMessage("Tenant updated successfully")
                                    }
                                },

                                onError = { error ->

                                    if (isAdded) {
                                        showMessage("Update failed: $error")
                                    }
                                }
                            )

                            dialog.dismiss()
                        }
                    }
                }
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(tenant: Tenant) {

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Tenant")
            .setMessage("Are you sure you want to delete ${tenant.name}?")
            .setPositiveButton("DELETE") { _, _ ->

                tenantViewModel.deleteTenant(
                    tenant,

                    onComplete = {

                        if (isAdded) {
                            showMessage("Tenant deleted successfully")
                        }
                    },

                    onError = { error ->

                        if (isAdded) {
                            showMessage("Deletion failed: $error")
                        }
                    }
                )
            }
            .setNegativeButton("CANCEL", null)
            .show()
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

        Log.d(
            "TENANT_LIFECYCLE",
            "ViewTenantsFragment: onDestroyView"
        )

        _binding = null
    }
}