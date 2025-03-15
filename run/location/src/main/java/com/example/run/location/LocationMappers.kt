package com.example.run.location

import android.location.Location
import com.example.core.domain.location.LocationWithAltitude
import com.example.core.domain.location.Location as CoreLocation

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = CoreLocation(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}