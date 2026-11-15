package com.example.tenantmanagmentsystem.data.repository

import androidx.lifecycle.LiveData
import com.example.tenantmanagmentsystem.data.local.TenantDao
import com.example.tenantmanagmentsystem.data.model.Tenant

class TenantRepository(private val tenantDao: TenantDao) {

    val tenants: LiveData<List<Tenant>> = tenantDao.getAllTenants()

    // Add tenant
    suspend fun addTenant(tenant: Tenant) {
        tenantDao.insertTenant(tenant)
    }

    // Update tenant
    suspend fun updateTenant(tenant: Tenant) {
        tenantDao.updateTenant(tenant)
    }

    // Delete tenant
    suspend fun deleteTenant(tenant: Tenant) {
        tenantDao.deleteTenant(tenant)
    }

    // Get tenant by ID
    suspend fun getTenantById(tenantId: Int): Tenant? {
        return tenantDao.getTenantById(tenantId)
    }
}