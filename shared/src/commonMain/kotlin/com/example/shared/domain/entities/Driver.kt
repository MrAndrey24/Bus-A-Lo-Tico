package com.example.shared.domain.entities

data class Driver(
    val id: String,
    val name: String,
    val companyId: String? = null,
)
