/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 */
package com.example.sharedlogic.data.local.user

import busalotico.data.local.AppDatabase

class LocalUserDataSource(database: AppDatabase) : ILocalUserDataSource {
  private val dbQuery = database.appDatabaseQueries

  override fun getFavoriteRouteIds(userId: String): Set<String> {
    return dbQuery.selectFavoriteRouteIds(userId).executeAsList().toSet()
  }

  override fun clearAndCreateFavorites(userId: String, remoteFavorites: Set<String>) {
    dbQuery.transaction {
      dbQuery.deleteFavoriteRoutesForUser(userId)
      remoteFavorites.forEach { routeId -> dbQuery.insertOrReplaceFavoriteRoute(userId, routeId) }
    }
  }

  override fun setRouteFavorite(userId: String, routeId: String, isFavorite: Boolean) {
    if (isFavorite) {
      dbQuery.insertOrReplaceFavoriteRoute(userId, routeId)
    } else {
      dbQuery.deleteFavoriteRoute(userId, routeId)
    }
  }
}
