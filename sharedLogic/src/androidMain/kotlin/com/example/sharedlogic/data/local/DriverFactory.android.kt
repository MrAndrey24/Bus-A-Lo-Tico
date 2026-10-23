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
package com.example.sharedlogic.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import busalotico.data.local.AppDatabase

actual class DriverFactory(val context: Context) {
  actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(AppDatabase.Schema, context, "busalotico.db")
  }
}
