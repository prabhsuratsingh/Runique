@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.example.run.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import com.example.core.domain.Timer
import com.example.core.domain.location.LocationTimestamp
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {

    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val isTracking = MutableStateFlow(false)

    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) locationObserver.observeLocation(1000L)
            else flowOf()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    init {
        isTracking
            .flatMapLatest { isTracking ->
                if(isTracking) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        currentLocation
            .filterNotNull()
            .combineTransform(isTracking){location, isTracking ->
                if(isTracking) emit(location)
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationTimestamp(
                    location = location,
                    durationTimestamp = elapsedTime
                )
            }
            .onEach { location ->
                val currentLocations = runData.value.locations
                val lastLocationList = if(currentLocations.isNotEmpty()) {
                    currentLocations.last() + location
                } else listOf(location)
                val newLocationList  = currentLocations.replaceLast(lastLocationList)

                val distanceMeters = LocationDataCalculator.getTotalDistanceMeters(newLocationList)
                val distanceKms = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp

                val avgSecondsPerKms = if(distanceKms == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKms).roundToInt()
                }

                _runData.update {
                    RunData(
                        distanceMeters = distanceMeters,
                        pace = avgSecondsPerKms.seconds,
                        locations = newLocationList
                    )
                }
            }
            .launchIn(applicationScope)
    }



    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun setIsTracking(isTracking: Boolean) {
        this.isTracking.value = isTracking
    }
}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>) : List<List<T>> {
    if(this.isEmpty()) return listOf(replacement)

    return this.dropLast(1) + listOf(replacement)
}