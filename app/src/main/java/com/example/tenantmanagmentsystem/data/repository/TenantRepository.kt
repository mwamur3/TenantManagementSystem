
package com.example.tenantmanagmentsystem.data.repository

import com.example.tenantmanagmentsystem.data.model.Tenant

object TenantRepository {

    val tenants = mutableListOf<Tenant>()

    fun addTenant(tenant: Tenant) {
        tenants.add(tenant)
    }

    fun totalRent(): Double {
        return tenants.sumOf {
            it.rent.toDoubleOrNull() ?: 0.0
        }
    }
}
