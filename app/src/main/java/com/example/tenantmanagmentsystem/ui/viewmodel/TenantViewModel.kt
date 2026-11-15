
package com.example.tenantmanagmentsystem.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tenantmanagmentsystem.data.model.Tenant
import com.example.tenantmanagmentsystem.data.repository.TenantRepository

class TenantViewModel : ViewModel() {

    private val _tenants = MutableLiveData<List<Tenant>>(
        TenantRepository.tenants.toList()
    )

    val tenants: LiveData<List<Tenant>> = _tenants

    private val _totalRent = MutableLiveData<Double>(
        TenantRepository.totalRent()
    )

    val totalRent: LiveData<Double> = _totalRent

    fun addTenant(tenant: Tenant) {
        TenantRepository.addTenant(tenant)
        _tenants.value = TenantRepository.tenants.toList()
        _totalRent.value = TenantRepository.totalRent()
    }
}
