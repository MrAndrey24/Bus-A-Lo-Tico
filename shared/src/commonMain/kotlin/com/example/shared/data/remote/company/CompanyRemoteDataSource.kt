/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.data.remote.company

import com.example.shared.data.remote.company.dto.CompanyDto
import com.example.shared.domain.entities.Company
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class CompanyRemoteDataSource : ICompanyRemoteDataSource {

  override suspend fun getCompany(companyId: String): Company? {
    val snapshot = Firebase.firestore.collection(COMPANIES_COLLECTION).document(companyId).get()

    if (!snapshot.exists) return null

    val dto = snapshot.data(CompanyDto.serializer())
    return dto.toDomain(snapshot.id)
  }

  override suspend fun getCompanies(): List<Company> {
    return Firebase.firestore
        .collection(COMPANIES_COLLECTION)
        .get()
        .documents
        .map { snapshot -> snapshot.data(CompanyDto.serializer()).toDomain(snapshot.id) }
        .sortedBy { it.name }
  }

  private companion object {
    const val COMPANIES_COLLECTION = "companies"
  }
}
