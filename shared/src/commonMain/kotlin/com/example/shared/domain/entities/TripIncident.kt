package com.example.shared.domain.entities

data class TripIncident(
    val id: String = "",
    val type: TripIncidentType,
    val detail: String,
    val createdAt: Long,
    val location: Coordinate? = null,
)

enum class TripIncidentType {
    ACCIDENT,
    BREAKDOWN,
    SECURITY,
}
