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
package com.example.sharedlogic.data.remote.driver

import com.example.sharedlogic.data.remote.driver.dto.DriverDto
import com.example.sharedlogic.domain.entities.Driver
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class DriverRemoteDataSource : IDriverRemoteDataSource {

  override suspend fun getDriver(driverId: String): Driver? {
    val snapshot = Firebase.firestore.collection(DRIVERS_COLLECTION).document(driverId).get()

    if (!snapshot.exists) return null

    val dto = snapshot.data(DriverDto.serializer())
    return dto.toDomain(snapshot.id)
  }

  override suspend fun getDriversByCompany(companyId: String): List<Driver> {
    return runCatching {
          Firebase.firestore
              .collection(DRIVERS_COLLECTION)
              .where { "companyId" equalTo companyId }
              .get()
              .documents
              .map { snapshot -> snapshot.data(DriverDto.serializer()).toDomain(snapshot.id) }
        }
        .getOrElse {
          Firebase.firestore
              .collection(DRIVERS_COLLECTION)
              .get()
              .documents
              .map { snapshot -> snapshot.data(DriverDto.serializer()).toDomain(snapshot.id) }
              .filter { it.companyId == companyId }
        }
  }

  override suspend fun upsertDriver(driverId: String, name: String, companyId: String?): Driver {
    val driverRef = Firebase.firestore.collection(DRIVERS_COLLECTION).document(driverId)

    val dto =
        DriverDto(
            name = name,
            companyId = companyId,
        )
    driverRef.set(dto)
    return dto.toDomain(driverId)
  }

  override suspend fun deleteDriver(driverId: String) {
    Firebase.firestore.collection(DRIVERS_COLLECTION).document(driverId).delete()
  }

  private companion object {
    const val DRIVERS_COLLECTION = "drivers"
  }
}
