/**
 * Copyright (c) 2026 (Andrey Acosta Ramirez, Isaac Felipe Castro Sanabria,
 * Kevin A. Hernández Rostrán, Maureen León Castro, Fabiana Milanés Sánchez)
 *
 * All Rights Reserved.
 *
 * This software is confidential and proprietary. Unauthorized copying,
 * distribution, or modification of this file is strictly prohibited.
 *
 * Current commercial development and maintenance managed by TuanisIO.
 */
package com.example.sharedlogic.data.repository.mapbox

internal object MapboxConstants {
  const val DIRECTIONS_BASE_URL = "https://api.mapbox.com/directions/v5/mapbox/driving/"
  const val GEOCODING_BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/"

  const val PARAM_ACCESS_TOKEN = "access_token"
  const val PARAM_GEOMETRIES = "geometries"
  const val PARAM_OVERVIEW = "overview"
  const val PARAM_LIMIT = "limit"
  const val PARAM_COUNTRY = "country"
  const val PARAM_PROXIMITY = "proximity"

  const val VALUE_GEOJSON = "geojson"
  const val VALUE_FULL = "full"
  const val VALUE_SEARCH_LIMIT = 5
  const val VALUE_COUNTRY_CR = "cr"
  const val VALUE_PROXIMITY_SJ = "-84.0833,9.9333"
}
