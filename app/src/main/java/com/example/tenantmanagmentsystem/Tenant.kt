package com.example.tenantmanagmentsystem

data class Tenant(
    val name: String,
    val phone: String,
    val rent: String
) {
    fun summary(): String {
        return "Tenant Name: $name\nPhone Number: $phone\nRent Paid: KSh $rent"
    }
}