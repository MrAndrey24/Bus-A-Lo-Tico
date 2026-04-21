/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.di.modules

import org.koin.dsl.module

val sharedModule = module { includes(dataModule, domainModule, presentationModule, networkModule) }
