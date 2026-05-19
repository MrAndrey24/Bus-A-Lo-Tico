/**
 * Copyright 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria, Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
