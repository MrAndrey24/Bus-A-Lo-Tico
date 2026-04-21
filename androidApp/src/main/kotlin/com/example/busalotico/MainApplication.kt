/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.busalotico

import android.app.Application
import com.example.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    initKoin {
      androidContext(this@MainApplication)
      androidLogger()
    }
  }
}
