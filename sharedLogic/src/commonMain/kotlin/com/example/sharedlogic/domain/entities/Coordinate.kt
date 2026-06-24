/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.domain.entities

import kotlin.math.*

data class Coordinate(val latitude: Double, val longitude: Double) {
  fun distanceTo(other: Coordinate): Double {
    val radiusOfEarthInKilometers = 6371.0

    val latitudeInRadians = this.latitude * PI / 180.0
    val otherLatitudeInRadians = other.latitude * PI / 180.0

    val deltaLatitudeInRadians = (other.latitude - this.latitude) * PI / 180.0
    val deltaLongitudeInRadians = (other.longitude - this.longitude) * PI / 180.0

    val haversineOfPhi =
        sin(deltaLatitudeInRadians / 2).pow(2) +
            cos(latitudeInRadians) *
                cos(otherLatitudeInRadians) *
                sin(deltaLongitudeInRadians / 2).pow(2)

    val angularDistanceInRadians = 2 * atan2(sqrt(haversineOfPhi), sqrt(1 - haversineOfPhi))

    return radiusOfEarthInKilometers * angularDistanceInRadians
  }
}
