package com.example.shared.data.remote

import com.example.shared.domain.entities.Company
import com.example.shared.domain.repository.ICompanyRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.Serializable

class CompanyFirestoreDataSource : ICompanyRepository {

    override suspend fun getCompany(companyId: String): Company? {
        val snapshot = Firebase.firestore
            .collection(COMPANIES_COLLECTION)
            .document(companyId)
            .get()

        if (!snapshot.exists) return null

        val dto = snapshot.data(CompanyDto.serializer())
        return dto.toDomain(snapshot.id)
    }

    @Serializable
    private data class CompanyDto(
        val name: String = ""
    ) {
        fun toDomain(documentId: String): Company = Company(
            id = documentId,
            name = name
        )
    }

    private companion object {
        const val COMPANIES_COLLECTION = "companies"
    }
}
