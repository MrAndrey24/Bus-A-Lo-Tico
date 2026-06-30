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
package com.example.busalotico.model

import androidx.compose.runtime.saveable.listSaver
import com.example.sharedlogic.domain.entities.Driver
import com.example.sharedlogic.domain.entities.Route
import com.example.sharedlogic.domain.entities.RouteLocation
import com.example.sharedlogic.domain.entities.RouteSchedule
import com.example.sharedlogic.domain.entities.RouteStop
import com.example.sharedlogic.util.normalizeTimeTo24Hour
import com.example.sharedlogic.util.parseFlexibleTimeToMinutes

enum class AdminSection {
  DASHBOARD,
  ROUTES,
  DRIVERS,
}

enum class RouteEditorTab {
  DETAILS,
  SCHEDULES,
  STOPS,
}

data class RouteFormState(
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val operator: String = "",
    val companyId: String = "",
    val startProvince: String = "",
    val startCanton: String = "",
    val startDistrict: String = "",
    val startLatitude: String = "",
    val startLongitude: String = "",
    val endProvince: String = "",
    val endCanton: String = "",
    val endDistrict: String = "",
    val endLatitude: String = "",
    val endLongitude: String = "",
    val fareCrc: String = "",
    val acceptsCardPayment: Boolean = false,
    val isActive: Boolean = true,
)

data class ScheduleFormState(
    val id: String = "",
    val driverId: String = "",
    val departureTime: String = "",
    val arrivalTime: String = "",
)

data class StopFormState(
    val id: String = "",
    val name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val order: String = "",
)

data class DriverFormState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
)

fun Route?.toFormState(): RouteFormState {
  if (this == null) return RouteFormState()
  return RouteFormState(
      id = id,
      code = code,
      name = name,
      operator = operator,
      companyId = companyId.orEmpty(),
      startProvince = startLocation.province.orEmpty(),
      startCanton = startLocation.canton.orEmpty(),
      startDistrict = startLocation.district.orEmpty(),
      startLatitude = startLocation.latitude?.toString().orEmpty(),
      startLongitude = startLocation.longitude?.toString().orEmpty(),
      endProvince = endLocation.province.orEmpty(),
      endCanton = endLocation.canton.orEmpty(),
      endDistrict = endLocation.district.orEmpty(),
      endLatitude = endLocation.latitude?.toString().orEmpty(),
      endLongitude = endLocation.longitude?.toString().orEmpty(),
      fareCrc = fareCrc?.toString().orEmpty(),
      acceptsCardPayment = acceptsCardPayment,
      isActive = isActive,
  )
}

fun RouteFormState.toRoute(): Route {
  return Route(
      id = id,
      code = code.trim(),
      name = name.trim(),
      companyId = companyId.trim().takeUnless { it.isBlank() },
      operator = operator.trim(),
      startLocation =
          RouteLocation(
              province = startProvince.trim().takeUnless { it.isBlank() },
              canton = startCanton.trim().takeUnless { it.isBlank() },
              district = startDistrict.trim().takeUnless { it.isBlank() },
              latitude = startLatitude.toDoubleOrNull(),
              longitude = startLongitude.toDoubleOrNull(),
          ),
      endLocation =
          RouteLocation(
              province = endProvince.trim().takeUnless { it.isBlank() },
              canton = endCanton.trim().takeUnless { it.isBlank() },
              district = endDistrict.trim().takeUnless { it.isBlank() },
              latitude = endLatitude.toDoubleOrNull(),
              longitude = endLongitude.toDoubleOrNull(),
          ),
      distanceKm = null,
      slopeCategory = null,
      pavedPercentage = null,
      surveyYear = null,
      fareCrc = fareCrc.toIntOrNull(),
      acceptsCardPayment = acceptsCardPayment,
      statusText = "ACTIVA",
      etaText = "",
      frequencyText = "",
      isActive = isActive,
      isLive = false,
      isFavorite = false,
      stops = emptyList(),
      schedules = emptyList(),
      polylinePoints = emptyList(),
  )
}

fun RouteSchedule.toFormState(): ScheduleFormState {
  return ScheduleFormState(
      id = id,
      driverId = driverId,
      departureTime = departureTime,
      arrivalTime = arrivalTime,
  )
}

fun ScheduleFormState.toSchedule(): RouteSchedule {
  return RouteSchedule(
      id = id,
      driverId = driverId.trim(),
      departureTime = normalizeTimeTo24Hour(departureTime.trim()) ?: departureTime.trim(),
      arrivalTime = normalizeTimeTo24Hour(arrivalTime.trim()) ?: arrivalTime.trim(),
  )
}

fun RouteStop.toFormState(): StopFormState {
  return StopFormState(
      id = id,
      name = name,
      latitude = latitude.toString(),
      longitude = longitude.toString(),
      order = order.toString(),
  )
}

fun StopFormState.toStop(): RouteStop {
  return RouteStop(
      id = id,
      name = name.trim(),
      latitude = latitude.toDoubleOrNull() ?: 0.0,
      longitude = longitude.toDoubleOrNull() ?: 0.0,
      order = order.toIntOrNull() ?: 0,
  )
}

fun String.toDisplayMeridiemTime(): String {
  val totalMinutes = parseFlexibleTimeToMinutes(this) ?: return this
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

fun Driver.toFormState(): DriverFormState {
  return DriverFormState(
      id = id,
      name = name,
      email = "",
      password = "",
  )
}

val RouteFormStateSaver =
    listSaver<RouteFormState, Any>(
        save = {
          listOf(
              it.id,
              it.code,
              it.name,
              it.operator,
              it.companyId,
              it.startProvince,
              it.startCanton,
              it.startDistrict,
              it.startLatitude,
              it.startLongitude,
              it.endProvince,
              it.endCanton,
              it.endDistrict,
              it.endLatitude,
              it.endLongitude,
              it.fareCrc,
              it.acceptsCardPayment,
              it.isActive,
          )
        },
        restore = {
          RouteFormState(
              id = it[0] as String,
              code = it[1] as String,
              name = it[2] as String,
              operator = it[3] as String,
              companyId = it[4] as String,
              startProvince = it[5] as String,
              startCanton = it[6] as String,
              startDistrict = it[7] as String,
              startLatitude = it[8] as String,
              startLongitude = it[9] as String,
              endProvince = it[10] as String,
              endCanton = it[11] as String,
              endDistrict = it[12] as String,
              endLatitude = it[13] as String,
              endLongitude = it[14] as String,
              fareCrc = it[15] as String,
              acceptsCardPayment = it[16] as Boolean,
              isActive = it[17] as Boolean,
          )
        },
    )

val ScheduleFormStateSaver =
    listSaver<ScheduleFormState, Any>(
        save = { listOf(it.id, it.driverId, it.departureTime, it.arrivalTime) },
        restore = {
          ScheduleFormState(it[0] as String, it[1] as String, it[2] as String, it[3] as String)
        },
    )

val StopFormStateSaver =
    listSaver<StopFormState, Any>(
        save = { listOf(it.id, it.name, it.latitude, it.longitude, it.order) },
        restore = {
          StopFormState(
              it[0] as String,
              it[1] as String,
              it[2] as String,
              it[3] as String,
              it[4] as String,
          )
        },
    )

val DriverFormStateSaver =
    listSaver<DriverFormState, Any>(
        save = { listOf(it.id, it.name, it.email, it.password) },
        restore = {
          DriverFormState(
              id = it[0] as String,
              name = it[1] as String,
              email = it[2] as String,
              password = it[3] as String,
          )
        },
    )
