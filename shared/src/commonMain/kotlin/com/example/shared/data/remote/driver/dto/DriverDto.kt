package com.example.shared.data.remote.driver.dto

import com.example.shared.domain.entities.Driver
import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    val name: String = "",
    val companyId: String? = null,
) {
    fun toDomain(driverId: String): Driver {
        return Driver(
            id = driverId,
            name = name,
            companyId = companyId,
        )
    }
}
