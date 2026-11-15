package com.example.tenantmanagmentsystem.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.tenantmanagmentsystem.data.local.TenantDatabase
import com.example.tenantmanagmentsystem.data.model.Tenant
import com.example.tenantmanagmentsystem.data.repository.TenantRepository
import kotlinx.coroutines.launch

class TenantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TenantRepository(
        TenantDatabase.getDatabase(application).tenantDao()
    )

    val tenants: LiveData<List<Tenant>> = repository.tenants

    val totalRent: LiveData<Double> = tenants.map { tenantList ->
        tenantList.sumOf {
            it.rent.replace(",", "").trim().toDoubleOrNull() ?: 0.0
        }
    }

    // Add tenant with error handling
    fun addTenant(
        tenant: Tenant,
        onComplete: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.addTenant(tenant)
                onComplete()
            } catch (e: Exception) {
                onError(e.message ?: "Unable to save tenant")
            }
        }
    }

    // Update tenant with error handling
    fun updateTenant(
        tenant: Tenant,
        onComplete: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.updateTenant(tenant)
                onComplete()
            } catch (e: Exception) {
                onError(e.message ?: "Unable to update tenant")
            }
        }
    }

    // Delete tenant with error handling
    fun deleteTenant(
        tenant: Tenant,
        onComplete: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteTenant(tenant)
                onComplete()
            } catch (e: Exception) {
                onError(e.message ?: "Unable to delete tenant")
            }
        }
    }

    fun getTenantById(
        tenantId: Int,
        onResult: (Tenant?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val tenant = repository.getTenantById(tenantId)
                onResult(tenant)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}