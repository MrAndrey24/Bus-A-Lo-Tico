package com.example.shared.data.remote.driver

import com.example.shared.data.remote.driver.dto.DriverDto
import com.example.shared.domain.entities.Driver
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class DriverRemoteDataSource : IDriverRemoteDataSource {

    override suspend fun getDriver(driverId: String): Driver? {
        val snapshot = Firebase.firestore
            .collection(DRIVERS_COLLECTION)
            .document(driverId)
            .get()

        if (!snapshot.exists) return null

        val dto = snapshot.data(DriverDto.serializer())
        return dto.toDomain(snapshot.id)
    }

    private companion object {
        const val DRIVERS_COLLECTION = "drivers"
    }
}
