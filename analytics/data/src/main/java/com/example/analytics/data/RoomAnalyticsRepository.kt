package com.example.analytics.data

import com.example.analytics.domain.AnalyticsRepository
import com.example.analytics.domain.AnalyticsValues
import com.example.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository (
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeMillis = async { analyticsDao.getTotalTimeRun() }
            val maxSpeedRun = async { analyticsDao.getMaxRunSpeed() }
            val avgDistancePerRun = async { analyticsDao.getAvgDistancePerRun() }
            val avgPacePerRun = async { analyticsDao.getAvgPacePerRun() }

            AnalyticsValues(
                totalDistance.await(),
                totalTimeMillis.await().milliseconds,
                maxSpeedRun.await(),
                avgDistancePerRun.await(),
                avgPacePerRun.await()
            )
        }
    }
}