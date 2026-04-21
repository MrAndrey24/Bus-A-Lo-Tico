/**
 * Copyright (c) 2026 Andrey Acosta Ramirez, Fabiana Milanés Sánchez, Isaac Felipe Castro Sanabria, Maureen León Castro
 * All Rights Reserved.
 */
package com.example.shared.util

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
