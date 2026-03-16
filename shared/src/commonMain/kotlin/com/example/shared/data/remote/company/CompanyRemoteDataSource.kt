package com.example.shared.data.remote.company

import com.example.shared.data.remote.company.dto.CompanyDto
import com.example.shared.domain.entities.Company
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class CompanyRemoteDataSource : ICompanyRemoteDataSource {

    override suspend fun getCompany(companyId: String): Company? {
        val snapshot = Firebase.firestore
            .collection(COMPANIES_COLLECTION)
            .document(companyId)
            .get()

        if (!snapshot.exists) return null

        val dto = snapshot.data(CompanyDto.serializer())
        return dto.toDomain(snapshot.id)
    }

    private companion object {
        const val COMPANIES_COLLECTION = "companies"
    }
}
