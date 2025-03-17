package com.example.run.domain

import com.example.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>) : Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext{l1, l2 ->
                    l1.location.location.distanceTo(l2.location.location)
                }.sum().roundToInt()
            }
    }
}