package com.example.shared.domain.entities

import kotlin.math.*

data class Coordinate(
    val latitude: Double, 
    val longitude: Double
) {
    fun distanceTo(other: Coordinate): Double {
        val radiusOfEarthInKilometers = 6371.0
        
        val latitudeInRadians = this.latitude * PI / 180.0
        val otherLatitudeInRadians = other.latitude * PI / 180.0
        
        val deltaLatitudeInRadians = (other.latitude - this.latitude) * PI / 180.0
        val deltaLongitudeInRadians = (other.longitude - this.longitude) * PI / 180.0

        val haversineOfPhi = sin(deltaLatitudeInRadians / 2).pow(2) +
                cos(latitudeInRadians) * cos(otherLatitudeInRadians) *
                sin(deltaLongitudeInRadians / 2).pow(2)

        val angularDistanceInRadians = 2 * atan2(sqrt(haversineOfPhi), sqrt(1 - haversineOfPhi))
        
        return radiusOfEarthInKilometers * angularDistanceInRadians
    }
}
