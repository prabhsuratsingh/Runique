package com.example.run.domain

import com.example.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>) : Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext{l1, l2 ->
                    l1.location.location.distanceTo(l2.location.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationTimestamp>>): Double {
        return locations.maxOf { locationSet ->
            locationSet.zipWithNext{ l1, l2 ->
                val distance = l1.location.location.distanceTo(
                    other = l2.location.location
                )
                val hoursDiff = (l2.durationTimestamp - l1.durationTimestamp)
                    .toDouble(DurationUnit.HOURS)

                if(hoursDiff == 0.0) {
                    0.0
                } else {
                    (distance / 1000.0) / hoursDiff
                }
            }.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf { locationSet ->
            locationSet.zipWithNext { l1, l2 ->
                val alt1 = l1.location.altitude
                val alt2 = l2.location.altitude
                (alt2 - alt1).coerceAtLeast(0.0)
            }.sum().roundToInt()
        }
    }
}