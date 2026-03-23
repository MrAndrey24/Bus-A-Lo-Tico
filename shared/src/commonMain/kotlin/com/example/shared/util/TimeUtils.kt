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
