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
