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
package com.example.shared.data.repository.mapbox

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
