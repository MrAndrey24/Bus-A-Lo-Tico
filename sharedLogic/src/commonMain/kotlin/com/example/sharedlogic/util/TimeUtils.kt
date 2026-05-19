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
package com.example.sharedlogic.util

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentCostaRicaTimeMinutes(): Int {
  val now = Clock.System.now()
  val tz = TimeZone.of("America/Costa_Rica")
  val local = now.toLocalDateTime(tz)
  return local.hour * 60 + local.minute
}

fun parseFlexibleTimeToMinutes(value: String): Int? {
  val normalized = value.trim().uppercase().replace(".", "").replace(Regex("\\s+"), " ")
  if (normalized.isBlank()) return null

  val twelveHourMatch = Regex("^(\\d{1,2}):(\\d{2})\\s?(AM|PM)$").matchEntire(normalized)
  if (twelveHourMatch != null) {
    val hourRaw = twelveHourMatch.groupValues[1].toIntOrNull() ?: return null
    val minute = twelveHourMatch.groupValues[2].toIntOrNull() ?: return null
    val suffix = twelveHourMatch.groupValues[3]
    if (hourRaw !in 1..12 || minute !in 0..59) return null
    val hour24 =
        when {
          suffix == "AM" && hourRaw == 12 -> 0
          suffix == "PM" && hourRaw != 12 -> hourRaw + 12
          else -> hourRaw
        }
    return hour24 * 60 + minute
  }

  val twentyFourHourMatch = Regex("^(\\d{1,2}):(\\d{2})$").matchEntire(normalized)
  if (twentyFourHourMatch != null) {
    val hour = twentyFourHourMatch.groupValues[1].toIntOrNull() ?: return null
    val minute = twentyFourHourMatch.groupValues[2].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    return hour * 60 + minute
  }

  return null
}

fun normalizeTimeTo24Hour(value: String): String? {
  val totalMinutes = parseFlexibleTimeToMinutes(value) ?: return null
  val hours = totalMinutes / 60
  val minutes = totalMinutes % 60
  return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}

fun formatTimeToMeridiem(value: String): String {
  val totalMinutes = parseFlexibleTimeToMinutes(value) ?: return value
  val hour24 = totalMinutes / 60
  val minutes = totalMinutes % 60
  val meridiem = if (hour24 >= 12) "PM" else "AM"
  val hour12 =
      when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
      }
  return "${hour12.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} $meridiem"
}
