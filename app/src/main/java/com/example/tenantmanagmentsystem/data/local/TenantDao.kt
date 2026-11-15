package com.example.tenantmanagmentsystem.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import com.example.tenantmanagmentsystem.data.model.Tenant

@Dao
interface TenantDao {

    // Insert a new tenant
    @Insert
    suspend fun insertTenant(tenant: Tenant)

    // Retrieve all tenants
    @Query("SELECT * FROM tenants ORDER BY name")
    fun getAllTenants(): LiveData<List<Tenant>>

    // Update an existing tenant
    @Update
    suspend fun updateTenant(tenant: Tenant)

    // Delete a tenant
    @Delete
    suspend fun deleteTenant(tenant: Tenant)

    // Retrieve a specific tenant using its ID
    @Query("SELECT * FROM tenants WHERE id = :tenantId")
    suspend fun getTenantById(tenantId: Int): Tenant?
}