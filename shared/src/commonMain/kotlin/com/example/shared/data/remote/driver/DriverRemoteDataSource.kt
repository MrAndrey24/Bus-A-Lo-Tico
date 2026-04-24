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
package com.example.shared.data.remote.driver

import com.example.shared.data.remote.driver.dto.DriverDto
import com.example.shared.domain.entities.Driver
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

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
